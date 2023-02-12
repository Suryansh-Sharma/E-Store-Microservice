package com.suryansh.reviewservice.mail;

import com.suryansh.reviewservice.config.RabbitMqConfig;
import com.suryansh.reviewservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMessageListener {
    private final MailService mailService;
    @RabbitListener(queues = RabbitMqConfig.QUEUE)
    public void listener(OrderPlacedMail message){
      log.info("Message received from Order Service.");
      mailService.sendOrderPlacedMail(message);
    }
}
