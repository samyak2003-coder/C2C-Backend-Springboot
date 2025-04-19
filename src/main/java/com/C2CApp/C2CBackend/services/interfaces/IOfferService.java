package com.C2CApp.C2CBackend.services.interfaces;

import java.util.List;
import java.util.Optional;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;
import com.C2CApp.C2CBackend.enums.OfferStatus;

public interface IOfferService {
    List<OfferSchema> getAllOffers();
    Optional<OfferSchema> getOfferById(String offerId);
    List<GetOfferResponse> getOfferByBuyerId(String buyerId);
    List<GetOfferResponse> getOfferBySellerId(String sellerId);
    OfferSchema createOffer(OfferSchema offer);
    void deleteOfferById(String offerId);
    OfferSchema updateOfferStatus(String offerId, OfferStatus status);
    void rejectAllPendingOffersForProduct(String productId, String excludeOfferId);
}
