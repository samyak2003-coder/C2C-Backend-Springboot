package com.C2CApp.C2CBackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.C2CApp.C2CBackend.dto.OfferWithProductDto;
import com.C2CApp.C2CBackend.entities.UpdateOfferInput;
import com.C2CApp.C2CBackend.repositories.OfferRepository;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.schema.ProductSchema;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final ProductService productService;

    @Autowired
    public OfferService(OfferRepository offerRepository, ProductService productService) {
        this.offerRepository = offerRepository;
        this.productService = productService;
    }

    public List<OfferSchema> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<OfferSchema> getOfferById(String offerId){
        return offerRepository.findByOfferId(offerId);
    }


    public List<OfferWithProductDto> getOfferByBuyerId(String buyerId) {
        List<OfferSchema> offers = offerRepository.findByBuyerIdCustom(buyerId);
        return offers.stream().map(offer -> {
            ProductSchema product = productService.getByProductId(offer.getProductId()).orElse(null);
            return new OfferWithProductDto(offer, 
                product != null ? product.getStatus() : "Unknown",
                product != null ? product.getTitle() : "Unknown");
        }).collect(java.util.stream.Collectors.toList());
    }

    public List<OfferWithProductDto> getOfferBySellerId(String sellerId) {
        List<OfferSchema> offers = offerRepository.findBySellerIdCustom(sellerId);
        return offers.stream().map(offer -> {
            ProductSchema product = productService.getByProductId(offer.getProductId()).orElse(null);
            return new OfferWithProductDto(offer, 
                product != null ? product.getStatus() : "Unknown",
                product != null ? product.getTitle() : "Unknown");
        }).collect(java.util.stream.Collectors.toList());
    }

    public boolean createOffer(OfferSchema offer){
        // Validate offer price is positive
        if (offer.getOfferedPrice() <= 0) {
            return false;
        }

        // Check if product exists and is available
        Optional<ProductSchema> product = productService.getByProductId(offer.getProductId());
        if (product.isEmpty() || "Sold".equals(product.get().getStatus())) {
            return false;
        }

        // Save the offer
        offerRepository.save(offer);
        return true;
    }

    public boolean updateOffer(String offerId, OfferSchema offer){
        Optional<OfferSchema> offerOptional = offerRepository.findById(offerId);
        if(offerOptional.isPresent()){
            OfferSchema updatedOffer = offerOptional.get();
            updatedOffer.setOfferedPrice(offer.getOfferedPrice());
            updatedOffer.setOfferDate(offer.getOfferDate());
            updatedOffer.setStatus(offer.getStatus());
            offerRepository.save(updatedOffer);
            return true;
        }
        return false;
    }

    public void deleteOfferById(String offerId){
        offerRepository.deleteByOfferId(offerId);
    }

    public void updateOfferStatus(String offerId, String status) {
        Optional<OfferSchema> offerOpt = offerRepository.findByOfferId(offerId);
        if (offerOpt.isPresent()) {
            OfferSchema offer = offerOpt.get();
            offerRepository.updateOfferStatus(offerId, status);
            // Update product status when offer is accepted
            if (status.equals("Accepted")) {
                String productId = offer.getProductId();
                System.out.println("Updating offer status to Accepted for offerId: " + offerId);
                System.out.println("Attempting to update product status for productId: " + productId);
                
                productService.getByProductId(productId)
                    .ifPresent(product -> {
                        System.out.println("Found product: " + productId + ", current status: " + product.getStatus());
                        product.setStatus("Sold");
                        productService.updateProduct(product.getProductId(), product);
                        System.out.println("Updated product status to Sold");
                        // Reject all other pending offers for this product
                        rejectAllPendingOffersForProduct(productId);
                        System.out.println("Rejected all other pending offers for product: " + productId);
                    });
            }
        }
    }

    public void rejectAllPendingOffersForProduct(String productId) {
        offerRepository.rejectAllPendingOffers(productId);
    }
}
