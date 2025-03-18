package com.C2CApp.C2CBackend.entities;

public class DeleteOfferDetails {
    private String offerId;

    public DeleteOfferDetails() {}

    public DeleteOfferDetails(String offerId){
        this.offerId = offerId;
    }

    public String getOfferId(){
        return offerId;
    }

    public void setOfferId(String offerId){
        this.offerId = offerId;
    }

}
