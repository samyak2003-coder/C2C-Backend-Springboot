package com.C2CApp.C2CBackend.dto.offer.create;

import java.util.Date;
import com.C2CApp.C2CBackend.enums.OfferStatus;

public class CreateOfferResponse {
    private String offerId;
    private String buyerId;
    private String sellerId;
    private String productId;
    private Double price;
    private Date createdDate;
    private OfferStatus status;

    // Constructors
    public CreateOfferResponse() {}

    public CreateOfferResponse(String offerId, String buyerId, String sellerId, 
                             String productId, Double price, Date createdDate, 
                             OfferStatus status) {
        this.offerId = offerId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.price = price;
        this.createdDate = createdDate;
        this.status = status;
    }

    // Getters and Setters
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }
}
