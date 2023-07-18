package com.suryansh.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class InventoryDocument {
    @Id
    private String id;
    // Seller
    private String sellerId;
    // Product
    private Product product;
    // Estimated Availability
    private EstimatedAvailability estimatedAvailability;
    // Shipping Options
    private List<ShippingOptions>shippingOptions;
    // Return Terms.
    private ReturnTerms returnTerms;
    // Market Price.
    private MarketPrice marketPrice;
}
