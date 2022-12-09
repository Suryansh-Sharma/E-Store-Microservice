package com.suryansh.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private String productName;
    private Long productId;
    private Boolean isInStock;
    private int noOfStock;
    private int totalSold;
}
