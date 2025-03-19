package com.C2CApp.C2CBackend.entities;

public class RemoveOfferDetails {
    private String offerId;

    public RemoveOfferDetails() {}

    public RemoveOfferDetails(String offerId){
        this.offerId = offerId;
    }

    public String getOfferId(){
        return offerId;
    }

    public void setOfferId(String offerId){
        this.offerId = offerId;
    }
}
