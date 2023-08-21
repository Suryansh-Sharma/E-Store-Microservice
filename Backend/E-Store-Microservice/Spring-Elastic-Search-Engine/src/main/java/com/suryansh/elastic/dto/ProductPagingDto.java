package com.suryansh.elastic.dto;

import com.suryansh.elastic.doc.ProductDoc;

import java.io.Serializable;
import java.util.List;

public record ProductPagingDto(int page_no, Long total_record, List<ProductDoc>productList
        ,int page_size,int total_pages) implements Serializable {
}
