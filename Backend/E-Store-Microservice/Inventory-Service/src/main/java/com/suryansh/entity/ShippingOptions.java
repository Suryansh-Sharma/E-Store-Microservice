package com.suryansh.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class ShippingOptions {
    private String shippingServiceCode;
    private String type;
    // Shipping Cost
    private ShippingCost shippingCost;
    private int quantityUsedForEstimate;
    private Instant minEstimatedDeliveryDate;
    private Instant maxEstimatedDeliveryDate;
    // AdditionalShippingCostPerUnit
    private AddiCostPerItem additionalCostPerItem;
    @Data
    public static class ShippingCost{
        private String value;
        private String currency;
    }
    @Data
    public static class AddiCostPerItem{
        private String value;
        private String currency;
    }
}
