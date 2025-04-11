package com.C2CApp.C2CBackend.dto;

import java.util.Date;

import com.C2CApp.C2CBackend.schema.OfferSchema;

public class OfferWithProductDto {
    private String offerId;
    private String productId;
    private String buyerId;
    private String sellerId;
    private Double offeredPrice;
    private Date offerDate;
    private String status;
    private String productStatus;
    private String productTitle;

    public OfferWithProductDto(OfferSchema offer, String productStatus, String productTitle) {
        this.offerId = offer.getOfferId();
        this.productId = offer.getProductId();
        this.buyerId = offer.getBuyerId();
        this.sellerId = offer.getSellerId();
        this.offeredPrice = offer.getOfferedPrice();
        this.offerDate = offer.getOfferDate();
        this.status = offer.getStatus();
        this.productStatus = productStatus;
        this.productTitle = productTitle;
    }

    // Getters
    public String getOfferId() { return offerId; }
    public String getProductId() { return productId; }
    public String getBuyerId() { return buyerId; }
    public String getSellerId() { return sellerId; }
    public Double getOfferedPrice() { return offeredPrice; }
    public Date getOfferDate() { return offerDate; }
    public String getStatus() { return status; }
    public String getProductStatus() { return productStatus; }
    public String getProductTitle() { return productTitle; }

    // Setters
    public void setOfferId(String offerId) { this.offerId = offerId; }
    public void setProductId(String productId) { this.productId = productId; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setOfferedPrice(Double offeredPrice) { this.offeredPrice = offeredPrice; }
    public void setOfferDate(Date offerDate) { this.offerDate = offerDate; }
    public void setStatus(String status) { this.status = status; }
    public void setProductStatus(String productStatus) { this.productStatus = productStatus; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }
}
