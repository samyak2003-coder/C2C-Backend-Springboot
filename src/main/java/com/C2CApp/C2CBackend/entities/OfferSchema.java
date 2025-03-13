package com.C2CApp.C2CBackend.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "offers")
public class OfferSchema {
    
    @Id
    @Column(nullable = false)
    private String offerId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String buyerId;

    @Column(nullable = false)
    private double offeredPrice;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date offerDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private String message;

    public OfferSchema() {
        this.offerId = UUID.randomUUID().toString();
    }

    public OfferSchema(String productId, String buyerId, double offeredPrice, Date offerDate, String status, String message) {
        this.offerId = UUID.randomUUID().toString();
        this.productId = productId;
        this.buyerId = buyerId;
        this.offeredPrice = offeredPrice;
        this.offerDate = offerDate;
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
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