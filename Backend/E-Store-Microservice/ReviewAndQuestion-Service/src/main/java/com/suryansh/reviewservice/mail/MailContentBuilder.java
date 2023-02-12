package com.suryansh.reviewservice.mail;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Service
@AllArgsConstructor
public class MailContentBuilder {
    private final TemplateEngine templateEngine;
    public String buildOlderPlaceMail(OrderPlacedMail mail){
        Context context = new Context();
        context.setVariable("email",mail.getEmail());
        context.setVariable("totalItem",mail.getTotalItem());
        context.setVariable("price",mail.getPrice());
        context.setVariable("totalPrice",mail.getTotalPrice());
        context.setVariable("line1",mail.getLine1());
        context.setVariable("city",mail.getCity());
        context.setVariable("pinCode",mail.getPinCode());
        context.setVariable("date",mail.getDate());
        return templateEngine.process("OrderPlaced",context);
    }
}
