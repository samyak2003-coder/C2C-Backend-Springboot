package com.C2CApp.C2CBackend.entities;

import java.util.Date;

public class CreateOfferResponse {
    private String productId;
    private String sellerId;
    private double offeredPrice;
    private Date offerDate;
    private String status;
    private String message;

    public CreateOfferResponse() {}

    public CreateOfferResponse(String productId, String sellerId, double offeredPrice,
                               Date offerDate, String status, String message) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.offeredPrice = offeredPrice;
        this.offerDate = offerDate;
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}