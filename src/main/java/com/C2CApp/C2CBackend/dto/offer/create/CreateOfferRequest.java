package com.C2CApp.C2CBackend.dto.offer.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class CreateOfferRequest {
    @NotNull
    @Positive
    private Double price;

    @NotNull
    private String productId;

    @NotNull
    private String sellerId;

    @NotNull
    private String buyerId;

    // Getters and Setters
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

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

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }


}
