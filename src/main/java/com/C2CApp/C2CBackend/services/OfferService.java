package com.C2CApp.C2CBackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.C2CApp.C2CBackend.entities.OfferSchema;
import com.C2CApp.C2CBackend.entities.UpdateOfferInput;
import com.C2CApp.C2CBackend.repositories.OfferRepository;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository){
        this.offerRepository = offerRepository;
    }

    public List<OfferSchema> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<OfferSchema> getOfferById(String offerId){
        return offerRepository.findByOfferId(offerId);
    }


    public List<OfferSchema> getOfferByBuyerId(String buyerId){
        return offerRepository.findByBuyerIdCustom(buyerId);
    }

    public List<OfferSchema> getOfferBySellerId(String sellerId){
        return offerRepository.findBySellerIdCustom(sellerId);
    }

    public void createOffer(OfferSchema offer){
        offerRepository.save(offer);
    }

    public void updateOffer(String offerId, OfferSchema offer){
        Optional<OfferSchema> offerOptional = offerRepository.findById(offerId);
        if(offerOptional.isPresent()){
            OfferSchema updatedOffer = offerOptional.get();
            updatedOffer.setOfferedPrice(offer.getOfferedPrice());
            updatedOffer.setOfferDate(offer.getOfferDate());
            updatedOffer.setStatus(offer.getStatus());
        }
    }

    public void deleteOfferById(String offerId){
        offerRepository.deleteByOfferId(offerId);
    }

    public void updateOfferStatus(String offerId, String status) {
        offerRepository.updateOfferStatus(offerId, status);
    }
}

