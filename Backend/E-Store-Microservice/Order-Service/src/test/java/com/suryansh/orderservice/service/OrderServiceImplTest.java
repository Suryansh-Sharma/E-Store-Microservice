package com.suryansh.orderservice.service;

import com.suryansh.orderservice.dto.OrderDto;
import com.suryansh.orderservice.dto.UserDto;
import com.suryansh.orderservice.entity.Order;
import com.suryansh.orderservice.exception.exception.SpringOrderException;
import com.suryansh.orderservice.repository.OrderAddressRepository;
import com.suryansh.orderservice.repository.OrderItemsRepository;
import com.suryansh.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {
    @Mock
    private OrderItemsRepository orderItemsRepository;
    @Mock
    private OrderAddressRepository orderAddressRepository;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    private String token="Bearer";
    @DisplayName("Get Order By Id : Success Scenario ")
    @Test
    void getOrderByIdTest(){
        // Mocking
        Order order = getMockOrder();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));
        // Actual
        OrderDto orderResponse = orderService.getOrderByOrderId(1L);

        // Verification
        verify(orderRepository,times(1))
                .findById(anyLong());
        // Assert
        assertNotNull(orderResponse);
        assertEquals(order.getId(),orderResponse.getId());

    }
    @Test
    @DisplayName("Get Order by id : Failure Scenario")
    void testGetOrderByIdNotFound(){
        // Mocking
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));
        // Actual
        SpringOrderException exception =
                assertThrows(SpringOrderException.class,
                        ()->orderService.getOrderByOrderId(1L));
        // Verification
        verify(orderRepository,times(1))
                .findById(anyLong());

    }
    @Test
    @DisplayName("For getting all order by user : Success Scenario ")
    public void getAllOrderByUserSuccessScenario(){
        // Mocking
        when(webClientBuilder.build().get()
                .uri("http://geekyprogrammer:8080/api/user/by-userName/" + "SuryanshSharma@1942")
                .header("Authorization",token)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.empty())
                .bodyToMono(UserDto.class)
                .block()
        ).thenReturn(getMockUserResponse());

    }

    private UserDto getMockUserResponse() {
        return UserDto.builder()
                .id(1L)
                .userName("SuryanshSharma@1942")
                .cartTotalPrice(200F)
                .cartTotalProducts(2)
                .totalLikedProduct(0)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .id(12L)
                .userId(1L)
                .orderDate(Instant.now())
                .lastUpdate(Instant.now())
                .status("Testing")
                .totalItems(2)
                .price(26300F)
                .isProductDelivered(false)
                .build();

    }

}