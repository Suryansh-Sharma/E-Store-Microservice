package com.suryansh.orderservice.service;

import com.suryansh.orderservice.config.RabbitMqConfig;
import com.suryansh.orderservice.dto.*;
import com.suryansh.orderservice.entity.Order;
import com.suryansh.orderservice.entity.OrderItem;
import com.suryansh.orderservice.exception.MicroserviceException;
import com.suryansh.orderservice.exception.SpringOrderException;
import com.suryansh.orderservice.mail.OrderPlacedMail;
import com.suryansh.orderservice.mapper.OrderServiceMapping;
import com.suryansh.orderservice.model.OrderUpdateModel;
import com.suryansh.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderServiceImpl implements OrderService {
    private final WebClient.Builder webClientBuilder;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final OrderServiceMapping mapping;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    public OrderServiceImpl(WebClient.Builder webClientBuilder,
                            OrderRepository orderRepository, RabbitTemplate rabbitTemplate, OrderServiceMapping mapping) {
        this.webClientBuilder = webClientBuilder;
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.mapping = mapping;
    }

    @Async
    @Transactional
    public CompletableFuture<String> placeOrder(String userName, String token) {
        var cart = webClientBuilder.build().get()
                .uri("http://USER-SERVICE/api/cart/of-user/" + userName)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new
                        MicroserviceException("Sorry cart service is down , try after some time !! : place order")))
                .bodyToMono(CartDto.class)
                .block();
        if (cart == null) {
            return CompletableFuture.failedFuture(new
                    SpringOrderException("User Cart is Empty. :placeOrder","CartEmpty", HttpStatus.NOT_FOUND));
        }

        List<OrderItem>orderItems = new ArrayList<>();
        for(CartItem item:cart.getCartProduct()){
            if (!item.getIsInStock()){
                return CompletableFuture.failedFuture(
                        new SpringOrderException("Sorry Product "+item.getProductName()+" is out of stock !!","ProductOutOfStock",HttpStatus.NOT_FOUND)
                );
            }else{
                orderItems.add(mapping.cartItemsToOrderItemEntity(item));
            }
        }

        // Calling User Microservice to get User By UserName.
        var user = webClientBuilder.build().get()
                .uri("http://USER-SERVICE/api/user/by-username/" + userName)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,clientResponse -> Mono.error(new
                        MicroserviceException("Sorry user service is down , please try after some time.")))
                .bodyToMono(UserProfileDto.class)
                .block();

        if (user == null) {
            return CompletableFuture.failedFuture(new
                    SpringOrderException("User is not Available. :placeOrder","UerNotFound",HttpStatus.NOT_FOUND));
        }

        var orderAddress = mapping.mapAddressDtoToEntity(user);

        var order = mapping.mapOrderModelToEntity(cart,user.id(),orderAddress,orderItems);
        try {
            orderRepository.save(order);

//            OrderItemModel orderItemModel =mapping.cartItemToInventory(cart.getCartProduct(),user.id());

            // Sending orderItemModel through KAFKA to inventory for updating after order placed.

            // Clearing cart of user after order placed through kafka.

            // Sending Order placed email to user through Kafka.

            logger.info("Order placed successfully by user {} ",user.id());
            return CompletableFuture.completedFuture("Order Placed Successfully !!");
        }catch (MicroserviceException e){
            logger.info("Micro Service is down "+e);
            throw e;
        }
        catch (Exception e) {
            logger.info("Unable to place order for user {} ",user.id(),e);
            throw new SpringOrderException("Unable to Place Order !! :placeOrder","SomethingWentWrong",HttpStatus.BAD_REQUEST);
        }
    }

    public void sendOrderPlacedEmail(Order order, UserProfileDto user){
        OrderPlacedMail orderPlacedMail = OrderPlacedMail.builder()
                .messageId(UUID.randomUUID().toString())
                .email(user.username())
                .totalItem(order.getTotalItems())
                .price(order.getPrice())
                .totalPrice(100F+150.00F+ order.getPrice())
                .line1(user.address().line1())
                .city(user.address().city())
                .pinCode(user.address().pinCode())
                .date(new Date())
                .build();
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE,
                RabbitMqConfig.ROUTING_KEY,orderPlacedMail);
        logger.info("Mail sent to Rabbit Queue for order placed.");
    }

    @Override
    public List<OrderDto> getAllOrderByUser(String userName, String token) {
        UserProfileDto user = webClientBuilder.build().get()
                .uri("http://USER-SERVICE/api/user/by-username/" + userName)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,clientResponse -> Mono.error(new
                        MicroserviceException("Sorry User-Service is down : get All order by user")))
                .bodyToMono(UserProfileDto.class)
                .block();
        if (user == null) throw new SpringOrderException("User is not Available. :placeOrder","UerNotFound",HttpStatus.NOT_FOUND);
        List<Order> orders = orderRepository.findByUserIdOrderByIdDesc(user.id());
        return orders.stream()
                .map(mapping::OrderEntityToDto)
                .toList();
    }

    @Override
    public OrderDetailDto getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order of Id : " + orderId +
                        " getOrderDetails","OrderNotFound",HttpStatus.NOT_FOUND));
        return mapping.orderEntityToDetail(order);
    }

    @Override
    public OrderDto getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order of Id : " + orderId +
                        " getOrderDetails","OrderNotFound",HttpStatus.NOT_FOUND));
        return mapping.OrderEntityToDto(order);
    }

    @Override
    public List<OrderDto> getAllPendingOrder() {
        return orderRepository.findByIsProductDelivered(false)
                .stream().map(mapping::OrderEntityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateOrder(OrderUpdateModel model) {
        Order order = orderRepository.findById(model.getOrderId())
                .orElseThrow(() -> new SpringOrderException("Unable to Find Order of Id : " + model.getOrderId() +
                        " getOrderDetails","OrderNotFound",HttpStatus.NOT_FOUND));
        order.setIsProductDelivered(model.getIsProductDelivered());
        order.setLastUpdate(Instant.now());
        order.setStatus(model.getStatus());
        try{
            orderRepository.save(order);
        }catch (Exception e){
            logger.info("Unable to update order "+e);
            throw new SpringOrderException("Unable to update product ","SomethingWentWrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void sendDummyMail() {
        OrderPlacedMail orderPlacedMail = OrderPlacedMail.builder()
                .messageId(UUID.randomUUID().toString())
                .email("suryanshsharma1942@gmail.com")
                .totalItem(2)
                .price(2500F)
                .totalPrice(100F+150.00F+ 2500F)
                .line1("(Rajesh Sharma) House no 4 Chaudhary Shikarpur Compound near D.M Residence")
                .city("Bulandshahr")
                .pinCode("203001")
                .date(new Date())
                .build();
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE,
                RabbitMqConfig.ROUTING_KEY,orderPlacedMail);
        logger.info("Mail sent to Rabbit Queue for order placed.");
    }





}
