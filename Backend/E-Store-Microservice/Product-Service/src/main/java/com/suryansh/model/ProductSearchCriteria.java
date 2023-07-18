package com.suryansh.model;

public record ProductSearchCriteria(String brand,
                                    String category,
                                    String sort_by,
                                    String sort_field,
                                    int page) {
}
