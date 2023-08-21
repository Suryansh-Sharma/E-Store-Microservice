package com.suryansh.elastic.dto;

import java.io.Serializable;
import java.util.List;

public record NavSearchProductDto(int page_no, long total_record
        , List<Product>productList, int total_page, int page_size) implements Serializable {
    public record Product(String id,String name)implements Serializable{}
}
