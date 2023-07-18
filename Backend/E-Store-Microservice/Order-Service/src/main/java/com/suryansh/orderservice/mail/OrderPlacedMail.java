package com.suryansh.orderservice.mail;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderPlacedMail {
    private String messageId;
    private String email;
    private int totalItem;
    private Float price;
    private Float totalPrice;
    private String line1;
    private String city;
    private String pinCode;
    private Date date;
}