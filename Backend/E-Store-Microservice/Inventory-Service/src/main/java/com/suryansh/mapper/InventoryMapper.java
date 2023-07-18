package com.suryansh.mapper;

import com.suryansh.dto.InventoryResponse;
import com.suryansh.entity.*;
import com.suryansh.model.InventoryModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryMapper {
    private final ModelMapper modelMapper;
    InventoryMapper(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }
    public InventoryDocument InventoryModelToEntity(InventoryModel model){
        InventoryDocument res = new InventoryDocument();
        res.setSellerId(model.getSellerId());
        res.setProduct(ProductModelToEntity(model.getProduct()));
        res.setEstimatedAvailability(EstimatedModelToEntity(model.getEstimatedAvailability()));
        res.setShippingOptions(
                model.getShippingOptions()
                        .stream()
                        .map(this::ShippingModelToEntity)
                        .toList()
        );
        res.setReturnTerms(ReturnModelToEntity(model.getReturnTerms()));
        res.setMarketPrice(MarketPriceModelToEntity(model.getMarketPrice()));
        return res;
    }

    private Product ProductModelToEntity(InventoryModel.InventoryProductModel product) {
        return modelMapper.map(product,Product.class);
    }

    private EstimatedAvailability EstimatedModelToEntity(InventoryModel.EstimatedAvailabilityModel model){
        return modelMapper.map(model,EstimatedAvailability.class);
    }
    private ShippingOptions ShippingModelToEntity(InventoryModel.ShippingOptionsModel model){
        return modelMapper.map(model,ShippingOptions.class);
    }
    private ReturnTerms ReturnModelToEntity(InventoryModel.ReturnTermsModel model){
        return modelMapper.map(model,ReturnTerms.class);
    }
    private MarketPrice MarketPriceModelToEntity(InventoryModel.MarketPriceModel marketPrice) {
        return modelMapper.map(marketPrice,MarketPrice.class);
    }

    public InventoryResponse InventoryEntityToDto(InventoryDocument document) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setProduct(ProductEntityToDto(document.getProduct()));
        inventoryResponse.setSellerId(document.getSellerId());
        inventoryResponse.setEstimatedAvailability(EstimatedEntityToDto(document.getEstimatedAvailability()));
        inventoryResponse.setShippingOptions(
                document.getShippingOptions()
                        .stream()
                        .map(this::ShippingEntityToDto)
                        .toList()
        );
        inventoryResponse.setReturnTerms(ReturnEntityToDto(document.getReturnTerms()));
        inventoryResponse.setMarketPrice(MarketPriceEntityToDto(document.getMarketPrice()));
        return inventoryResponse;
    }
    private InventoryResponse.ProductDto ProductEntityToDto(Product product){
        return modelMapper.map(product,InventoryResponse.ProductDto.class);
    }
    private InventoryResponse.EstimatedAvailabilityDto EstimatedEntityToDto(EstimatedAvailability entity) {
        return modelMapper.map(entity, InventoryResponse.EstimatedAvailabilityDto.class);
    }

    private InventoryResponse.ShippingOptionsDto ShippingEntityToDto(ShippingOptions entity) {
        return modelMapper.map(entity, InventoryResponse.ShippingOptionsDto.class);
    }

    private InventoryResponse.ReturnTermsDto ReturnEntityToDto(ReturnTerms entity) {
        return modelMapper.map(entity, InventoryResponse.ReturnTermsDto.class);
    }

    private InventoryResponse.MarketPriceDto MarketPriceEntityToDto(MarketPrice entity) {
        return modelMapper.map(entity, InventoryResponse.MarketPriceDto.class);
    }

}
