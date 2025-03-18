package com.C2CApp.C2CBackend.entities;

public class UpdateOfferInput {
    private String offerId;
    private String status;

    public UpdateOfferInput() {}

    public UpdateOfferInput(String offerId, String status){
        this.offerId = offerId;
        this.status = status;
    }

    public String getOfferId(){
        return offerId;
    }

    public void setOfferId(String offerId){
        this.offerId = offerId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

}
