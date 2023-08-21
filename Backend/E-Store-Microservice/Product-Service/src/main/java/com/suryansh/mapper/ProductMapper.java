package com.suryansh.mapper;

import com.suryansh.dto.InventoryResponse;
import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductFullDto;
import com.suryansh.dto.ProductRatingDto;
import com.suryansh.entity.*;
import com.suryansh.model.ElasticSearchProductModel;
import com.suryansh.model.ProductModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product ProductModelToEntity(ProductModel model, Brand brand) {
        Product product = modelMapper.map(model, Product.class);
        Price price = convertPriceModelToEntity(model.getPrice());
        ProductRichText productRichText = convertRichTextModelToEntity(model.getRichText());
        DiscountPrice discountPrice = convertDiscountModelToEntity(model.getDiscountPrice());
        List<ProductImage> productImages = model.getAdditionalImages()
                .stream()
                .map(image->ProductImage.builder()
                        .imageUrl(image.getImageUrl())
                        .imageType(image.getImageType())
                        .build() )
                .toList();
        product.setCategoryPath(model.getCategoryTree());
        product.setBrand(brand);
        product.setPrice(price);
        product.setDiscountPrice(discountPrice);
        product.setProductImages(productImages);
        product.setProductRichText(productRichText);
        return product;
    }

    public ProductRichText convertRichTextModelToEntity(ProductModel.RichTextModel richText) {
        return modelMapper.map(richText, ProductRichText.class);
    }

    public DiscountPrice convertDiscountModelToEntity(ProductModel.DiscountPrice discountPrice) {
        return modelMapper.map(discountPrice,DiscountPrice.class);
    }

    public Price convertPriceModelToEntity(ProductModel.Price priceModel) {
        return modelMapper.map(priceModel, Price.class);
    }

    public ProductFullDto convertProductFullEntityToDto(Product product,
                                                        InventoryResponse inventoryResponse, ProductRatingDto productRatingDto) {
        ProductFullDto productFullDto = modelMapper.map(product, ProductFullDto.class);
        productFullDto.setInventoryResponse(inventoryResponse);
        productFullDto.setProductRatingDto(productRatingDto);
        return productFullDto;
    }

    public ProductDto convertProductEntityToDto(Product product) {
        var priceDto = new  ProductDto.Price(
                product.getDiscountPrice().getValue(),product.getDiscountPrice().getCurrency());
        return new ProductDto(product.getId(),product.getTitle(),product.getShortDescription(),priceDto,
                product.getCategoryPath(), product.getImageUrl(), product.getColor(), product.getItemWebUrl(), product.getDescription()
        );
    }

    public ElasticSearchProductModel convertProductEntityToElasticDoc(Product model
                                                                      ,ProductModel productModel){
        return ElasticSearchProductModel.builder()
                .id(String.valueOf(model.getId()))
                .name(model.getTitle())
                .image(model.getImageUrl())
                .brand(model.getBrand().getName())
                .rating(productModel.getRatingAndReview().getRating())
                .ratingCount(productModel.getRatingAndReview().getRatingCount())
                .reviewCount(productModel.getRatingAndReview().getReviewCount())
                .price(new ElasticSearchProductModel.Price(
                        model.getPrice().getCurrency(),
                        model.getPrice().getValue()
                ))
                .newPrice(new
                        ElasticSearchProductModel
                                .Price(model.getDiscountPrice().getCurrency(),model.getDiscountPrice().getValue()))
                .discount(model.getDiscountPrice().getPercentage())
                .categoryTree(model.getCategoryPath())
                .shortDescription(model.getShortDescription())
                .filters(productModel.getFilterOptions())
                .build();
    }
}
