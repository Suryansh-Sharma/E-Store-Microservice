package com.suryansh.dto;

import com.suryansh.entity.ShippingOptions;
import lombok.Data;

import java.util.List;


@Data
public class InventoryResponse {
    private String sellerId;
    // Product
    private ProductDto product;
    // Estimated Availability
    private EstimatedAvailabilityDto estimatedAvailability;
    // Shipping Options
    List<ShippingOptionsDto> shippingOptions;
    // Return Terms.
    private ReturnTermsDto returnTerms;
    // Market Price.
    private MarketPriceDto marketPrice;

    @Data
    public static class ProductDto{
        private Long productId;
        private String title;
    }

    @Data
    public static class EstimatedAvailabilityDto {
        private int soldQuantity;
        private boolean isInStock;
        private int totalStock;
        private AvailabilityStatus availabilityStatus;
        public enum AvailabilityStatus{
            IN_STOCK,
            OUT_OF_STOCK,
            COMING_SOON,
            DISCONTINUED
        }
    }
    @Data
    public static class ShippingOptionsDto {
        private String providerName;
        private String type;
        // Shipping Cost
        private ShippingOptions.ShippingCost shippingCost;
        private int quantityUsedForEstimate;
        private String minEstimatedDeliveryDate;
        private String maxEstimatedDeliveryDate;
        // AdditionalShippingCostPerUnit
        private ShippingOptions.AddiCostPerItem additionalCostPerItem;
        @Data
        public static class ShippingCost{
            private Double value;
            private String currency;
        }
        @Data
        public static class AddiCostPerItem{
            private Double value;
            private String currency;
        }
    }
    @Data
    public static class ReturnTermsDto {
        private String returnAccepted;
        private String refundMethod;
        private String returnPeriod;
        private String returnInstructions;
    }
    @Data
    public static class MarketPriceDto {
        private OriginalPrice originalPrice;
        private String discountPercentage;
        private DiscountAmount discountAmount;
        @Data
        public static class OriginalPrice{
            private String value;
            private String currency;
        }
        @Data
        public static class DiscountAmount{
            private String value;
            private String currency;
        }
    }
}