package com.suryansh.elastic.service;

import com.suryansh.elastic.doc.ProductDoc;
import com.suryansh.elastic.dto.NavSearchProductDto;
import com.suryansh.elastic.dto.ProductDto;
import com.suryansh.elastic.dto.ProductPagingDto;
import com.suryansh.elastic.exception.SpringElasticExcep;
import com.suryansh.elastic.mapper.ProductMapper;
import com.suryansh.elastic.repository.ProductRepo;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService{
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    public ProductServiceImpl(ProductRepo productRepo, ProductMapper productMapper, ElasticsearchOperations elasticsearchOperations) {
        this.productRepo = productRepo;
        this.productMapper = productMapper;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public ProductDto saveNewProduct(ProductDto dto) {
        var productDoc = productRepo.findById(dto.getId());
        if (productDoc.isPresent() && productDoc.get().getName().equals(dto.getName().toUpperCase())){
            throw new SpringElasticExcep("Sorry Product of this Id is already present ","ProductPresent",
                    HttpStatus.CONFLICT);
        }
        var newProduct = productMapper.mapProductDtoToDoc(dto);
        try{
            productRepo.save(newProduct);
            logger.info("Product {} is added to elasticsearch ",dto.getId());
            return dto;
        }catch (Exception e){
            logger.error("Unable to save product "+e);
            throw new SpringElasticExcep("Unable to save product","SomethingWentWrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProductPagingDto getAllProductFromDb(int page_no, int page_size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        Pageable pageable = PageRequest.of(page_no,page_size);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(searchSourceBuilder.query())
                .build();

        SearchHits<ProductDoc> searchHits = elasticsearchOperations.search(query, ProductDoc.class);

        final List<ProductDoc> productDocs = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        long totalRecords = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalRecords / page_size);

        return new ProductPagingDto(page_no+1, totalRecords, productDocs, page_size, totalPages);

    }

    @Override
    public ProductDoc updateProductInDb(String id, ProductDto dto) {
        Optional<ProductDoc> productDoc = productRepo.findById(id);
        if (productDoc.isEmpty()){
            throw new SpringElasticExcep("Unable to find product for update",
                    "ProductNotFound", HttpStatus.NOT_FOUND);
        }
        ProductDoc newProduct = productMapper.mapProductDtoToDoc(dto);
        newProduct.setId(id);
        try {
            productRepo.save(newProduct);
            logger.info("Product {} is updated successfully", newProduct.getId());
            return newProduct;
        } catch (Exception e) {
            logger.error("Unable to update product {}", newProduct, e);
            throw new SpringElasticExcep("Unable to update product " + id,
                    "UnableToUpdateProduct", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NavSearchProductDto getNavSearchFuzzy(String value, String field, int pageNo, int pageSize) {
        logger.info("Caching check");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name",value)
                .fuzziness(Fuzziness.AUTO);
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        searchQueryBuilder
                .withQuery(matchQueryBuilder)
                .withPageable(pageable);

        SearchHits<ProductDoc> searchHits = elasticsearchOperations
                .search(searchQueryBuilder.build(), ProductDoc.class);
        List<NavSearchProductDto.Product>products = new ArrayList<>();

        for (SearchHit<ProductDoc> searchHit : searchHits) {
            ProductDoc productDoc = searchHit.getContent();
            products.add(new NavSearchProductDto.Product(productDoc.getId(),productDoc.getName()));
        }

        long totalRecords = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        return new NavSearchProductDto(pageNo+1, totalRecords, products, totalPages,pageSize);

    }


    @Override
    public Map<String, List<String>> getFilterFromDbForSearch(String data) {
        Pageable pageable = PageRequest.of(0,20);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .should(QueryBuilders.matchQuery("name", data)
                                        .analyzer("edge_ngram_analyzer"))
                                .should(QueryBuilders.fuzzyQuery("name", data)
                                        .fuzziness(Fuzziness.AUTO))
                )
                .withPageable(pageable)
                .build();
        SearchHits<ProductDoc>searchHits = elasticsearchOperations
                .search(searchQuery,ProductDoc.class);

        List<ProductDoc> productDocs = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();
        Map<String, List<String>> filterMap = new HashMap<>();

        for (ProductDoc productDoc : productDocs) {
            List<ProductDoc.Filter> filters = productDoc.getFilters();

            for (ProductDoc.Filter filter : filters) {
                String key = filter.getKey();
                String value = filter.getValue();

                List<String> values = filterMap.computeIfAbsent(key, k -> new ArrayList<>());
                if (!values.contains(value)) {
                    values.add(value);
                }
            }

            for(ProductDoc.Category category: productDoc.getCategories()){
                String key = "categories";
                String name = category.getName();

                List<String> names = filterMap.computeIfAbsent(key, k -> new ArrayList<>());
                if (!names.contains(name)){
                    names.add(name);
                }
            }
        }


        return filterMap;
    }

    @Override
    public ProductPagingDto getFullTextSearchFromElasticDb(
            String field, String data, Map<String, String> filters, String sort_by, String sort_direction
            , String categories, int pageSize, int pageNo, String is_filter_applied) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.queryStringQuery(field+":"+data+"*")) ;

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();

        Pageable pageable = PageRequest.of(pageNo,pageSize);

        if(!sort_by.equals("none")&&!sort_direction.equals("none")){
            if(sort_direction.equals("asc")){
                pageable = PageRequest.of(pageNo,pageSize, Sort.by(sort_by).ascending());
            }else if (sort_direction.equals("desc")){
                pageable = PageRequest.of(pageNo,pageSize, Sort.by(sort_by).descending());
            }
        }

        if (!categories.equals("none")){
            BoolQueryBuilder filterQuery = QueryBuilders.boolQuery();
            filterQuery.should(QueryBuilders.termQuery("categories.name.keyword",categories));
            searchQueryBuilder.withFilter(filterQuery);
        }


        if (is_filter_applied.equals("true") && !filters.isEmpty()) {
            BoolQueryBuilder filtersQuery = QueryBuilders.boolQuery();

            filters.forEach((key, value) -> {
                if (key != null && value != null) {
                    filtersQuery.must(QueryBuilders.termQuery("filters.key.keyword", key))
                            .must(QueryBuilders.termQuery("filters.value.keyword", value));
                }
            });

            searchQueryBuilder.withFilter(filtersQuery);
        }


        searchQueryBuilder
                .withPageable(pageable)
                .withQuery(boolQueryBuilder);
        SearchHits<ProductDoc>searchHits = elasticsearchOperations
                .search(searchQueryBuilder.build(),ProductDoc.class);

        List<ProductDoc> productDocs = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        long totalRecords = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        return new ProductPagingDto(pageNo+1, totalRecords, productDocs, pageSize, totalPages);
    }

    @Override
    public List<ProductDto> saveNewMultiProduct(List<ProductDto> dto) {
        List<ProductDoc>productDocs = dto.stream()
                .map(productMapper::mapProductDtoToDoc)
                .toList();
        try{
            productRepo.saveAll(productDocs);
            logger.info("Many Product saved ");
            return dto;
        }catch (Exception e){
            logger.error("Unable to save product "+e);
            throw new SpringElasticExcep("Unable to save product","SomethingWentWrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ProductPagingDto getSimilarCategoryProd(String category, int pageNo,
                                                   int pageSize, String sortBy) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("categoryTree",category);
        NativeSearchQueryBuilder nativeSearchQueryBuilder= new NativeSearchQueryBuilder();
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        if (!sortBy.equals("none")){
            pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy).descending());
        }
        nativeSearchQueryBuilder
                .withQuery(matchQueryBuilder)
                .withPageable(pageable);

        SearchHits<ProductDoc>searchHits = elasticsearchOperations
                .search(nativeSearchQueryBuilder.build(),ProductDoc.class);

        List<ProductDoc>productDocs = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        long totalRecords = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        return new ProductPagingDto(pageNo+1, totalRecords, productDocs, pageSize, totalPages);

    }

    @Override
    public void updateProductRatingInDb(String id, Double rating) {
        ProductDoc productDoc = productRepo.findById(id)
                .orElseThrow(()->new SpringElasticExcep("Unable to find product for updating rating","ProductNotFound",
                        HttpStatus.NOT_FOUND));
        if (rating<0 || rating>5){
            throw new SpringElasticExcep("Product rating should be in range of 0-5","ProductRatingNotInRange",HttpStatus.BAD_REQUEST);
        }
        productDoc.setRating(rating);
        try {
            productRepo.save(productDoc);
            logger.info("Product {} rating is updated successfully to {} ",id,rating);
        }catch (Exception e){
            logger.error("Unable to update product rating "+e);
        }
    }

    @Override
    public void updateStockOfProductInDb(String id, int sold, Boolean isInStock) {
        ProductDoc productDoc = productRepo.findById(id)
                .orElseThrow(()->new SpringElasticExcep("Unable to find product for updating rating","ProductNotFound",
                        HttpStatus.NOT_FOUND));
        if (sold<=0){
            throw new SpringElasticExcep("Product sold quantity can't be less than or equal to 0","SoldQuantityNotInRange",
                    HttpStatus.BAD_REQUEST);
        }
        productDoc.setTotalSold(productDoc.getTotalSold()+sold);
        productDoc.setInStock(isInStock);
        try {
            productRepo.save(productDoc);
            logger.info("Successfully updated product {} sold-quantity{} and stock-status{} ",productDoc.getId(),productDoc.getTotalSold()
            ,productDoc.isInStock());
        }catch (Exception e){
            logger.error("Unable to update product stock status "+e);
        }
    }

    @Override
    public NavSearchProductDto getProductWithFuzzyAndSuggest(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .should(QueryBuilders.matchQuery("name", name)
                                        .analyzer("edge_ngram_analyzer"))
                                .should(QueryBuilders.fuzzyQuery("name", name)
                                        .fuzziness(Fuzziness.AUTO))
                )
                .withPageable(pageable)
                .build();
        SearchHits<ProductDoc>searchHits = elasticsearchOperations
                .search(searchQuery,ProductDoc.class);

        List<NavSearchProductDto.Product>products = new ArrayList<>();

        for (SearchHit<ProductDoc> searchHit : searchHits) {
            ProductDoc productDoc = searchHit.getContent();
            products.add(new NavSearchProductDto.Product(productDoc.getId(),productDoc.getName()));
        }

        long totalRecords = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        return new NavSearchProductDto(pageNo+1, totalRecords, products, totalPages,pageSize);
    }

}
