package com.C2CApp.C2CBackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.C2CApp.C2CBackend.entities.OfferSchema;
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

    public List<OfferSchema> getOffers() {
        return offerRepository.findAll();
    }

    public List<OfferSchema> getOfferByBuyerId(String buyerId){
        return offerRepository.findByBuyerId(buyerId);
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
            updatedOffer.setMessage(offer.getMessage());
        }
    }

    public void deleteOffer(String offerId){
        offerRepository.deleteById(offerId);
    }
}
