package com.suryansh.reviewservice.service;

import com.suryansh.reviewservice.mail.MailContentBuilder;
import com.suryansh.reviewservice.mail.OrderPlacedMail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailContentBuilder contentBuilder;
    @Async
    public void sendOrderPlacedMail(OrderPlacedMail message) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(message.getEmail());
            helper.setSubject("Order Placed Successfully");
            String content = contentBuilder
                    .buildOlderPlaceMail(message);
            helper.setText(content,true);
        };
        try{
            javaMailSender.send(mimeMessagePreparator);
        }catch (MailException e){
            log.error("Error during sending order placed mail.");
            System.out.println(e);
            throw new MailSendException("Unable to send mail");
        }
    }
}
