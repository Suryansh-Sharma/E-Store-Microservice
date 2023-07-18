package com.suryansh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
public class SellerOrderDocument {
    @Id
    private String id;
    private int totalOrder;
    List<OrderDetail>orderDetails;
}


