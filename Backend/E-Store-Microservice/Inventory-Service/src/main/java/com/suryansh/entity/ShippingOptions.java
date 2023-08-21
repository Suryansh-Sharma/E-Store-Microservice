package com.suryansh.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingOptions {
    private String providerName;
    private String type;
    // Shipping Cost
    private ShippingCost shippingCost;
    private int quantityUsedForEstimate;
    private String minEstimatedDeliveryDate;
    private String maxEstimatedDeliveryDate;
    // AdditionalShippingCostPerUnit
    private AddiCostPerItem additionalCostPerItem;
    @Data
    @Builder
    public static class ShippingCost{
        private Double value;
        private String currency;
    }
    @Data
    @Builder
    public static class AddiCostPerItem{
        private Double value;
        private String currency;
    }
}
