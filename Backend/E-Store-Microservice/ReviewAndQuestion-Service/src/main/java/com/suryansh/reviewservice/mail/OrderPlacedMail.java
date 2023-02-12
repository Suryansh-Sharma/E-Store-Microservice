package com.suryansh.reviewservice.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
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
    private int pinCode;
    private Date date;
}