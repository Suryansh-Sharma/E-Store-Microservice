package com.suryansh.entity;

import lombok.Data;

@Data
public class EstimatedAvailability {
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

