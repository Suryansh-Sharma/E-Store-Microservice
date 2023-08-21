package com.suryansh.elastic.controller;

import com.suryansh.elastic.doc.ProductDoc;
import com.suryansh.elastic.dto.NavSearchProductDto;
import com.suryansh.elastic.dto.ProductDto;
import com.suryansh.elastic.dto.ProductPagingDto;
import com.suryansh.elastic.service.ProductService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the rest-controller class that handles add,get,fuzzy-search,filter product.
 * This class also uses REDIS for caching mechanism.
 *
 * @author suryansh
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/elastic/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * This method is used for adding new product in an elasticsearch database.
     *
     * @param dto It accepts productDto as request-body with Hibernate validation.
     * @return ProductDto It will return ProductDto after saving product in a database successfully.
     */
    @PostMapping("/add-new")
    public ProductDto addNewProduct(@Valid @RequestBody ProductDto dto) {
        return productService.saveNewProduct(dto);
    }

    /**
     * This method is used for adding multiple new product in an elasticsearch database.
     *
     * @param dto It accepts the List of productDto as request-body with Hibernate validation.
     * @return List<ProductDto> It will return the List of ProductDto after saving product in a database successfully.
     */
    @PostMapping("/add-new/multiple")
    public List<ProductDto> addNewMultipleProduct(@Valid @RequestBody List<ProductDto> dto) {
        return productService.saveNewMultiProduct(dto);
    }

    /**
     * This method is used to get all products in a database.
     * This method also uses REDIS for caching data.
     *
     * @param page_no   It denotes the page number got pagination.
     * @param page_size It denotes the page size for pagination.
     * @return ProductPagingDto It return product paging dto with data,total data,page size,page no.
     */
    @GetMapping("/all")
    @Cacheable(value = "all-products",key = "#page_no + '-' + #page_size")
    public ProductPagingDto getAllProducts(@RequestParam(required = false, defaultValue = "0") int page_no,
                                            @RequestParam(required = false, defaultValue = "6") int page_size) {
        return productService.getAllProductFromDb(page_no, page_size);
    }

    /**
     * This method is used for adding updating product in an elasticsearch database.
     *
     * @param id  It accepts String id of product that is going to be updated.
     * @param dto It accepts productDto as request-body with Hibernate validation.
     * @return ProductDoc It will return ProductDto after saving product in a database successfully.
     */
    @PostMapping("/update/{id}")
    public ProductDoc updateProduct(@PathVariable String id, @RequestBody @Valid ProductDto dto) {
        return productService.updateProductInDb(id, dto);
    }

    /**
     * This method is use for navbar search operation.
     * This method uses a fuzzy-search query.
     * This method also uses REDIS for caching.
     *
     * @param value     It is the value that is going to search.
     * @param field     It is the target in document e.g:- name,description,brand etc.
     * @param page_no   It denotes the page number got pagination.
     * @param page_size It denotes the page size for pagination.
     * @return ProductPagingDto It return product paging dto with data,total data,page size,page no.
     */
    @GetMapping("/nav-fuzzy-search/{value}")
    @Cacheable(value = "product-fuzzy-search", key = "#value + '-' + #page_no + '-' + #page_size", unless = "#value.length() <= 3")
    public NavSearchProductDto getNavFuzzySearch(@PathVariable String value,
                                              @RequestParam(required = false, defaultValue = "name") String field,
                                              @RequestParam(required = false, defaultValue = "0") int page_no,
                                              @RequestParam(required = false, defaultValue = "6") int page_size) {
        return productService.getNavSearchFuzzy(value, field, page_no, page_size);
    }

    /**
     * This method is used for full text searching.
     *
     * @param field             It is the target in document e.g:- name,description,brand etc.
     * @param data              It is the value that is going to search.
     * @param page_no           It denotes the page number got pagination.
     * @param page_size         It denotes the page size for pagination.
     * @param sort_by           It is used for denoting sorting field e.g:- price,rating etc.
     * @param sort_direction    It denotes the direction of sorting e.g:- Ascending,Descending.
     * @param categories          It is used for filtering based on category.
     * @param is_filter_applied It tells weather filter is applied or not.
     * @param allParams         It is Map<Key,Value> that contains filter-key and filter-value.
     * @return ProductPagingDto It return product paging dto with data,total data,page size,page no.
     */
    @GetMapping("/full-text-search")
    public ProductPagingDto fullTextSearch(@RequestParam(required = false, defaultValue = "name") String field
            , @RequestParam String data
            , @RequestParam(required = false, defaultValue = "6") int page_size
            , @RequestParam(required = false, defaultValue = "0") int page_no
            , @RequestParam(required = false, defaultValue = "none") String sort_by
            , @RequestParam(required = false, defaultValue = "none") String sort_direction
            , @RequestParam(required = false, defaultValue = "none") String categories
            , @RequestParam(required = false, defaultValue = "false") String is_filter_applied
            , @RequestParam Map<String, String> allParams) {

        // Remove the filter-related parameters from the allParams map
        allParams.remove("field");
        allParams.remove("data");
        allParams.remove("page_size");
        allParams.remove("page_no");
        allParams.remove("sort_by");
        allParams.remove("sort_direction");
        allParams.remove("categories");
        allParams.remove("is_filter_applied");

        // Set the remaining parameters as filters
        Map<String, String> filters = new HashMap<>(allParams);

        return productService.getFullTextSearchFromElasticDb(field, data, filters, sort_by,
                sort_direction, categories, page_size, page_no, is_filter_applied);
    }

    /**
     * This method is used to get filters for search.
     * This method uses REDIS for caching.
     *
     * @param data It is the value that is going to search.
     * @return It returns Map<Title,List<Filters>> as a result.
     */
    @GetMapping("/full-text-search/get-filters/{data}")
    @Cacheable(value = "filter-by-fuzzy", key = "#data", unless = "#data.length() <=3")
    public Map<String, List<String>> getFiltersForFuzzySearch(@PathVariable String data) {
        return productService.getFilterFromDbForSearch(data);
    }

    /**
     * This method returns product who has a similar category tree.
     * This method uses REDIS for caching.
     *
     * @param category  It is the category of product e.g:- Electronics >> Audio >> Headphone.
     * @param page_no   It denotes the page number got pagination.
     * @param page_size It denotes the page size for pagination.
     * @param sort_by   It is used for denoting sorting field e.g:- price,rating etc.
     * @return ProductPagingDto It return product paging dto with data,total data,page size,page no.
     */
    @GetMapping("/get-similar-products/by-category-tree/{category}")
    @Cacheable(value = "similar-cate-product", key = "#category + '-' + #page_no + '-' + #page_size + '-' + #sort_by")
    public ProductPagingDto productBySimilarCategory(@PathVariable String category,
                                                     @RequestParam(required = false, defaultValue = "0") int page_no,
                                                     @RequestParam(required = false, defaultValue = "6") int page_size,
                                                     @RequestParam(required = false, defaultValue = "none") String sort_by) {
        return productService.getSimilarCategoryProd(category, page_no, page_size, sort_by);
    }

    @GetMapping("/search-with-elastic-fuzzy-and-suggestion/{name}")
    @Cacheable(value = "nav-search-fuzzy-suggester",key = "#name + '-' + #page_no + '-' + #page_size",unless = "#name.length()<=3")
    public NavSearchProductDto searchWithFuzzyAndSuggestion(@PathVariable String name,
                                                            @RequestParam(required = false, defaultValue = "0") int page_no,
                                                            @RequestParam(required = false, defaultValue = "6") int page_size) {
        return productService.getProductWithFuzzyAndSuggest(name,page_no,page_size);
    }

    @PostMapping("/update-rating-of-product/{id}/{rating}")
    public void updateRatingOfProduct(@PathVariable String id,@PathVariable Double rating){
         productService.updateProductRatingInDb(id,rating);
    }

    @PostMapping("/update/sold-no/stock-status/{id}/{sold}/{isInStock}")
    public void updateStockStatus(@PathVariable String id,@PathVariable int sold,@PathVariable Boolean isInStock){
        productService.updateStockOfProductInDb(id,sold,isInStock);
    }
}
