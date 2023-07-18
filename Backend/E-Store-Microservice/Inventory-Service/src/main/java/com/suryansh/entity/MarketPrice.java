package com.suryansh.entity;

import lombok.Data;

@Data
public class MarketPrice {
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
