package com.suryansh.entity;

import lombok.Data;

@Data
public class MarketPrice {
    private OriginalPrice originalPrice;
    private float discountPercentage;
    private DiscountAmount discountAmount;
    @Data
    public static class OriginalPrice{
        private Double value;
        private String currency;
    }
    @Data
    public static class DiscountAmount{
        private Double value;
        private String currency;
    }
}
