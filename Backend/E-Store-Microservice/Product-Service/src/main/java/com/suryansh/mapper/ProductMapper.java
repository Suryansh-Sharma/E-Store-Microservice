package com.suryansh.mapper;

import com.suryansh.dto.InventoryResponse;
import com.suryansh.dto.ProductDto;
import com.suryansh.dto.ProductFullDto;
import com.suryansh.dto.ProductRatingDto;
import com.suryansh.entity.*;
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

    public Product ProductModelToEntity(ProductModel model, Brand brand, ProductBelongsTo belongsTo) {
        Product product = modelMapper.map(model, Product.class);
        Price price = convertPriceModelToEntity(model.getPrice());
        List<ProductImage> productImages = model.getProductImages()
                .stream()
                .map(image->ProductImage.builder()
                        .imageUrl(image.getImageUrl())
                        .imageType(image.getImageType())
                        .build() )
                .toList();
        product.setBelongsTo(belongsTo);
        product.setBrand(brand);
        product.setPrice(price);
        product.setProductImages(productImages);
        return product;
    }

    public Price convertPriceModelToEntity(ProductModel.Price priceModel) {
        return modelMapper.map(priceModel, Price.class);
    }

    public ProductImage convertProductImageModelToEntity(ProductModel.ProductImage productImage) {
        return modelMapper.map(productImage, ProductImage.class);
    }

    public ProductFullDto convertProductFullEntityToDto(Product product,
                                                        InventoryResponse inventoryResponse, ProductRatingDto productRatingDto) {
        ProductFullDto productFullDto = modelMapper.map(product, ProductFullDto.class);
        productFullDto.setInventoryResponse(inventoryResponse);
        productFullDto.setProductRatingDto(productRatingDto);
        return productFullDto;
    }

    private ProductFullDto.Price convertPriceEntityToDto(Price price) {
        return modelMapper.map(price, ProductFullDto.Price.class);
    }

    private ProductFullDto.Brand convertBrandEntityToDto(Brand brand) {
        return modelMapper.map(brand, ProductFullDto.Brand.class);
    }

    private ProductFullDto.ProductImage convertImageEntityToDto(ProductImage image) {
        return modelMapper.map(image, ProductFullDto.ProductImage.class);
    }

    private ProductFullDto.ProductBelongsTo convertBelongsEntityToDto(ProductBelongsTo productBelongsTo) {
        return modelMapper.map(productBelongsTo, ProductFullDto.ProductBelongsTo.class);
    }

    public ProductDto convertProductEntityToDto(Product product) {
        var priceDto = new  ProductDto.Price(
                product.getPrice().getValue(),product.getPrice().getCurrency());
        return new ProductDto(product.getId(),product.getTitle(),product.getSubTitle(),product.getShortDescription(),priceDto,
                product.getCategoryPath(), product.getImageUrl(), product.getColor(), product.getItemWebUrl(), product.getDescription()
        );
    }
}
