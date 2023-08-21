package com.suryansh.elastic.doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This is the product entity/document that stores data in elasticsearch.
 * This class uses index name as products.
 */
@Data
@Builder
@Document(indexName = "products")
public class ProductDoc implements Serializable {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Date)
    private Date date;

    @Field(type = FieldType.Text,analyzer = "ngram_analyzer")
    private String name;

    private String image;

    @Field(type = FieldType.Text)
    private String brand;

    @Field(type = FieldType.Double)
    private Double rating;

    @Field(type = FieldType.Long)
    private Long ratingCount;

    @Field(type = FieldType.Long)
    private long viewCount;

    @Field(type = FieldType.Integer)
    private int review;

    @Field(type = FieldType.Long)
    private long totalSold;

    @Field(type = FieldType.Boolean)
    private boolean isInStock;

    @Field(type = FieldType.Float)
    private Float discount;

    @Field(type = FieldType.Keyword)
    private String categoryTree;

    @Field(type = FieldType.Text)
    private String description;

    private Price price;

    private Price newPrice;

    @Field(type = FieldType.Nested)
    private List<Category>categories;

    @Field(type = FieldType.Nested)
    private List<Filter> filters;

    @Data
    @AllArgsConstructor
    public static class Price implements Serializable{
        private String currency;
        @Field(type = FieldType.Double)
        private Double value;
    }

    @Data
    @AllArgsConstructor
    public static class Filter implements Serializable{
        private String key;
        private String value;
    }
    @Data
    @AllArgsConstructor
    public static class Category implements Serializable{
        private String name;
    }
}

