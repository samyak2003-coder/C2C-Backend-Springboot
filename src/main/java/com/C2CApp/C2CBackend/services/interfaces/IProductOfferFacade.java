package com.C2CApp.C2CBackend.services.interfaces;

import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.enums.ProductStatus;

import java.util.List;
import java.util.Optional;

public interface IProductOfferFacade {
    // Product-related operations
    Optional<ProductSchema> findProductById(String productId);
    void validateProductAvailabilityForOffer(String productId);
    void markProductAsSold(String productId);
    
    // Offer-related operations
    List<GetOfferResponse> getOffersForProduct(String productId);
    void rejectAllPendingOffersForProduct(String productId); 
    
    // Combined operations
    void handleOfferAcceptance(String offerId, String productId);
    void updateProductStatusAndOffers(String productId, ProductStatus status);
}
