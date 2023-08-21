package com.suryansh.elastic.mapper;

import com.suryansh.elastic.doc.ProductDoc;
import com.suryansh.elastic.dto.ProductDto;
import com.suryansh.elastic.exception.SpringElasticExcep;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used for performing mapping operations.
 * This maps entity-to-dto & model-to-entity.
 */
@Service
public class ProductMapper {
    public ProductDoc mapProductDtoToDoc(ProductDto dto) {
        List<ProductDoc.Filter> filters = dto.getFilters().stream()
                .map(this::mapFilterDtoToEntity)
                .toList();
        List<ProductDoc.Category> categories = new ArrayList<>();
        String[] categoryString = dto.getCategoryTree().split(" >> ");
        for (String c : categoryString) {
            ProductDoc.Category category = new ProductDoc.Category(c);
            categories.add(category);
        }
        Date date = Date.from(Instant.now());
        return ProductDoc.builder()
                .id(dto.getId())
                .date(date)
                .name(dto.getName().toUpperCase())
                .image(dto.getImage())
                .brand(dto.getBrand().toUpperCase())
                .rating(dto.getRating())
                .ratingCount(dto.getRatingCount())
                .viewCount(0L)
                .review(dto.getReviewCount())
                .totalSold(0)
                .description(dto.getShortDescription().toUpperCase())
                .isInStock(true)
                .price(mapPriceDtoToDoc(dto.getPrice()))
                .newPrice(mapPriceDtoToDoc(dto.getNewPrice()))
                .discount(dto.getDiscount())
                .categoryTree(dto.getCategoryTree())
                .categories(categories)
                .filters(filters)
                .build();
    }

    public ProductDoc.Price mapPriceDtoToDoc(ProductDto.Price price) {
        if (price.getValue() <= 0) {
            throw new SpringElasticExcep("Please enter same amount as of original if discount is 0"
                    , "DiscountPriceIsEmpty", HttpStatus.CONFLICT);
        }
        return new ProductDoc.Price(price.getCurrency().toUpperCase(), price.getValue());
    }

    public ProductDoc.Filter mapFilterDtoToEntity(ProductDto.Filter filter) {
        return new ProductDoc.Filter(filter.getKey().toUpperCase(), filter.getValue().toUpperCase());
    }
}
