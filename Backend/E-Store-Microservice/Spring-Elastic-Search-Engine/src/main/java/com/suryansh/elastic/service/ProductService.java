package com.suryansh.elastic.service;

import com.suryansh.elastic.doc.ProductDoc;
import com.suryansh.elastic.dto.NavSearchProductDto;
import com.suryansh.elastic.dto.ProductPagingDto;
import com.suryansh.elastic.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductDto saveNewProduct(ProductDto dto);

    ProductPagingDto getAllProductFromDb(int page_no, int page_size);

    ProductDoc updateProductInDb(String id, ProductDto dto);
    NavSearchProductDto getNavSearchFuzzy(String value, String field, int pageNo, int pageSize);

    Map<String, List<String>> getFilterFromDbForSearch(String data);

    ProductPagingDto getFullTextSearchFromElasticDb(String field, String data, Map<String, String> filters, String sort, String sort_direction, String categories, int pageSize, int pageNo, String is_filter_applied);

    List<ProductDto> saveNewMultiProduct(List<ProductDto> dto);

    ProductPagingDto getSimilarCategoryProd(String category, int pageNo, int pageSize, String sortBy);

    void updateProductRatingInDb(String id, Double rating);

    void updateStockOfProductInDb(String id, int sold, Boolean isInStock);

    NavSearchProductDto getProductWithFuzzyAndSuggest(String name, int pageNo, int pageSize);
}
