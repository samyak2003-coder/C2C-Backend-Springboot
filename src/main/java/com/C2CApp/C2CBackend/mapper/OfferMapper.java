package com.C2CApp.C2CBackend.mapper;

import com.C2CApp.C2CBackend.dto.offer.create.CreateOfferRequest;
import com.C2CApp.C2CBackend.dto.offer.create.CreateOfferResponse;
import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;
import com.C2CApp.C2CBackend.dto.offer.update.UpdateOfferResponse;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.schema.ProductSchema;

public class OfferMapper {
    
    private OfferMapper() {
        // Private constructor to prevent instantiation
    }

    public static OfferSchema toEntity(CreateOfferRequest request) {
        return OfferSchema.builder() 
                .buyerId(request.getBuyerId())
                .sellerId(request.getSellerId())
                .productId(request.getProductId())
                .price(request.getPrice())
                .build();
    }

    public static CreateOfferResponse toCreateResponse(OfferSchema offer) {
        return new CreateOfferResponse(
            offer.getId(),
            offer.getBuyerId(),
            offer.getSellerId(),
            offer.getProductId(),
            offer.getPrice(),
            offer.getCreatedDate(),
            offer.getStatus()
        );
    }

    public static GetOfferResponse toGetResponse(OfferSchema offer, ProductSchema product) {
        return new GetOfferResponse(
            offer.getId(),
            offer.getBuyerId(),
            offer.getSellerId(),
            offer.getProductId(),
            product != null ? product.getTitle() : "Unknown",
            offer.getPrice(),
            offer.getCreatedDate(),
            offer.getStatus(),
            product != null ? product.getStatus() : null
        );
    }

    public static UpdateOfferResponse toUpdateResponse(OfferSchema offer) {
        return UpdateOfferResponse.success(offer.getId(), offer.getStatus());
    }

    public static UpdateOfferResponse toUpdateErrorResponse(String offerId, String message) {
        return new UpdateOfferResponse(offerId, null, message);
    }
}
