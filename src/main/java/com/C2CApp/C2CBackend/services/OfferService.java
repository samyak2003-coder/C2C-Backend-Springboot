package com.C2CApp.C2CBackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;
import com.C2CApp.C2CBackend.mapper.OfferMapper;
import com.C2CApp.C2CBackend.exceptions.BusinessException;
import com.C2CApp.C2CBackend.repositories.OfferRepository;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.services.interfaces.IOfferService;
import com.C2CApp.C2CBackend.enums.OfferStatus;
import com.C2CApp.C2CBackend.services.interfaces.IProductOfferFacade;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService implements IOfferService {
    private final OfferRepository offerRepository;
    private final IProductOfferFacade productOfferFacade;

    @Autowired
    public OfferService(OfferRepository offerRepository, IProductOfferFacade productOfferFacade) {
        this.offerRepository = offerRepository;
        this.productOfferFacade = productOfferFacade;
    }

    public List<OfferSchema> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<OfferSchema> getOfferById(String offerId) {
        return offerRepository.findById(offerId);
    }

    public List<GetOfferResponse> getOfferByBuyerId(String buyerId) {
        List<OfferSchema> offers = offerRepository.findByBuyerId(buyerId);
        return mapOffersToResponses(offers);
    }

    public List<GetOfferResponse> getOfferBySellerId(String sellerId) {
        List<OfferSchema> offers = offerRepository.findBySellerId(sellerId);
        return mapOffersToResponses(offers);
    }

    public List<GetOfferResponse> getOffersByProductId(String productId) {
        List<OfferSchema> offers = offerRepository.findByProductId(productId);
        return mapOffersToResponses(offers);
    }

    // New method to get raw offers without product enrichment
    public List<OfferSchema> findOffersByProductIdRaw(String productId) {
        return offerRepository.findByProductId(productId);
    }

    private List<GetOfferResponse> mapOffersToResponses(List<OfferSchema> offers) {
        return offers.stream()
            .map(offer -> {
                ProductSchema product = productOfferFacade.findProductById(offer.getProductId()).orElse(null);
                return OfferMapper.toGetResponse(offer, product);
            })
            .collect(java.util.stream.Collectors.toList());
    }

    public OfferSchema createOffer(OfferSchema offer) {
        // Validate offer price is positive
        if (offer.getPrice() <= 0) {
            throw new BusinessException("Offer price must be greater than zero");
        }

        // Check if product exists and is available
        productOfferFacade.validateProductAvailabilityForOffer(offer.getProductId());

        // Save the offer
        return offerRepository.save(offer);
    }

    public void deleteOfferById(String offerId) {
        offerRepository.deleteById(offerId);
    }

    @Override
    public OfferSchema updateOfferStatus(String offerId, OfferStatus status) {
        Optional<OfferSchema> offerOpt = offerRepository.findById(offerId);
        if (offerOpt.isEmpty()) {
            throw new BusinessException("Offer not found");
        }
    
        OfferSchema offer = offerOpt.get();
        
        // Only update the status in the repository
        offerRepository.updateStatus(offerId, status);
        offer.setStatus(status);  // Update the local object's status
        
        return offer;
    }

    @Override
    public void rejectAllPendingOffersForProduct(String productId, String excludeOfferId) {
        offerRepository.rejectAllPendingOffers(productId, OfferStatus.REJECTED, OfferStatus.PENDING, excludeOfferId);
    }
}
