package com.suryansh.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class InventoryModel {
    @NotBlank
    private String sellerId;

    @Valid
    @NotNull
    private InventoryProductModel product;

    @Valid
    @NotNull
    private EstimatedAvailabilityModel estimatedAvailability;

    @Valid
    @Size(min = 1)
    private List<ShippingOptionsModel> shippingOptions;

    @Valid
    @NotNull
    private ReturnTermsModel returnTerms;

    @Valid
    @NotNull
    private MarketPriceModel marketPrice;

    @Data
    public static class InventoryProductModel {
        @NotNull
        private Long productId;
        @NotBlank
        private String title;
    }

    @Data
    public static class EstimatedAvailabilityModel {
        private int soldQuantity;
        private int totalStock;
        private boolean isInStock;
        private AvailabilityStatus availabilityStatus;
        public enum AvailabilityStatus{
            IN_STOCK,
            OUT_OF_STOCK,
            COMING_SOON,
            DISCONTINUED
        }
    }
    @Data
    public static class ShippingOptionsModel {
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
    @Data
    public static class ReturnTermsModel {
        private String returnAccepted;
        private String refundMethod;
        private String returnPeriod;
        private String returnInstructions;
    }
    @Data
    public static class MarketPriceModel {
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
