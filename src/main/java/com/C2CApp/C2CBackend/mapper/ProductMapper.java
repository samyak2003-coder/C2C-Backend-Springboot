package com.C2CApp.C2CBackend.mapper;

import java.util.List;
import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;
import com.C2CApp.C2CBackend.dto.product.request.CreateProductRequest;
import com.C2CApp.C2CBackend.dto.product.request.UpdateProductDetailsRequest;
import com.C2CApp.C2CBackend.dto.product.response.ProductResponse;
import com.C2CApp.C2CBackend.dto.product.response.ProductSummaryResponse;
import com.C2CApp.C2CBackend.enums.ProductStatus;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.services.OfferService;

public class ProductMapper {
    
    private ProductMapper() {
        // Private constructor to prevent instantiation
    }

    public static ProductSchema toEntity(CreateProductRequest request, String sellerId) {
        return ProductSchema.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .price(request.getPrice())
            .category(request.getCategory())
            .productCondition(request.getProductCondition())
            .sellerId(sellerId)
            .build();
    }

    public static ProductSchema toEntity(CreateProductRequest request, String sellerId, ProductStatus status) {
        ProductSchema product = toEntity(request, sellerId);
        product.setStatus(status);
        return product;
    }

    public static ProductSchema updateEntity(ProductSchema existingProduct, UpdateProductDetailsRequest request) {
        existingProduct.setTitle(request.getTitle());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setProductCondition(request.getProductCondition());
        return existingProduct;
    }

    public static ProductResponse toResponse(ProductSchema schema) {
        ProductResponse response = new ProductResponse();
        response.setId(schema.getId());
        response.setTitle(schema.getTitle());
        response.setDescription(schema.getDescription());
        response.setPrice(schema.getPrice());
        response.setCategory(schema.getCategory());
        response.setProductCondition(schema.getProductCondition());
        response.setStatus(schema.getStatus());
        response.setSellerId(schema.getSellerId());
        response.setCreatedDate(schema.getCreatedDate().toString());
        return response;
    }

    public static ProductSummaryResponse toSummaryResponse(ProductSchema schema) {
        ProductSummaryResponse response = new ProductSummaryResponse();
        response.setId(schema.getId());
        response.setTitle(schema.getTitle());
        response.setDescription(schema.getDescription());
        response.setPrice(schema.getPrice());
        response.setCategory(schema.getCategory());
        response.setStatus(schema.getStatus());
        response.setSellerId(schema.getSellerId());
        response.setCreatedDate(schema.getCreatedDate());
        return response;
    }
}
