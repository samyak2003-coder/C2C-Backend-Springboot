package com.C2CApp.C2CBackend.entities;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import java.util.Date;

public class CreateOfferInput {
    private String productId;
    private String sellerId;
    private Double offeredPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date offerDate;
    String token;

    @Column(nullable = false)
    private String status = "Not accepted"; 

    public CreateOfferInput() {}

    public CreateOfferInput(String productId, String sellerId, Double offeredPrice,
                            Date offerDate, String token, String status) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.offeredPrice = offeredPrice;
        this.offerDate = offerDate;
        this.token = token;
        this.status = status;
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

    public Double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(Double offeredPrice) {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}