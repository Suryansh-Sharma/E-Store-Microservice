package com.suryansh.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class InventoryModel {
    @NotBlank(message = "Seller id can't be blank")
    private String sellerId;

    private InventoryProductModel product;

    @Valid
    @NotNull
    private EstimatedAvailabilityModel estimatedAvailability;

    @Valid
    private List<ShippingOptionsModel> shippingOptions;

    @Valid
    @NotNull
    private ReturnTermsModel returnTerms;

    @Valid
    @NotNull
    private MarketPriceModel marketPrice;

    @Data
    public static class InventoryProductModel {
        private Long productId;
        private String title;
    }

    @Data
    public static class EstimatedAvailabilityModel {
        @Min(value = 0,message = "Sold quantity can't be less than 0")
        private int soldQuantity;
        @Min(value = 0,message = "Total stock can't be less than 0")
        private int totalStock;
        private boolean isInStock;
        private AvailabilityStatus availabilityStatus;

        public enum AvailabilityStatus {
            IN_STOCK,
            OUT_OF_STOCK,
            COMING_SOON,
            DISCONTINUED
        }
    }

    @Data
    public static class ShippingOptionsModel {
        @NotBlank(message = "Shipping provider name can't be blank")
        private String providerName;
        @NotBlank(message = "Shipping option type can't be blank")
        private String type;
        // Shipping Cost
        @Valid
        private ShippingCost shippingCost;
        @Min(value = 0,message = "Quantity of estimate can't be less than 0")
        private int quantityUsedForEstimate;
        @NotBlank(message = "Min estimate delivery date can't be blank")
        private String minEstimatedDeliveryDate;
        @NotBlank(message = "Max estimate delivery date can't be blank")
        private String maxEstimatedDeliveryDate;
        // AdditionalShippingCostPerUnit
        @Valid
        private AddiCostPerItem additionalCostPerItem;

        @Data
        public static class ShippingCost {
            @Min(value = 0,message = "Shipping cost value can't be less than 0")
            private Double value;
            @NotBlank(message = "Shipping cost currency can't be blank")
            private String currency;
        }

        @Data
        public static class AddiCostPerItem {
            @Min(value = 0,message = "Additional shipping cost value can't be less than 0")
            private Double value;
            @NotBlank(message = "Additional shipping cost currency can't be blank")
            private String currency;
        }
    }

    @Data
    public static class ReturnTermsModel {
        @NotBlank(message = "Return Accepted can't be blank")
        private String returnAccepted;
        @NotBlank(message = "Return Method can't be blank")
        private String refundMethod;
        @NotBlank(message = "Return Period can't be blank")
        private String returnPeriod;
        @NotBlank(message = "Return Instruction can't be blank")
        private String returnInstructions;
    }

    @Data
    public static class MarketPriceModel {
        @Valid
        @NotNull
        private OriginalPrice originalPrice;
        @Min(value = 0,message = "Discount percentage can't be less than 0")
        private float discountPercentage;
        @Valid
        @NotNull
        private DiscountAmount discountAmount;

        @Data
        public static class OriginalPrice {
            @Min(value = 0,message = "Original price value can't be less than 0")
            private Double value;
            @NotBlank(message = "Original price currency can't be blank")
            private String currency;
        }

        @Data
        public static class DiscountAmount {
            @Min(value = 0,message = "Discount price  value can't be less than 0")
            private Double value;
            @NotBlank(message = "Discount price currency can't be blank")
            private String currency;
        }
    }
}
