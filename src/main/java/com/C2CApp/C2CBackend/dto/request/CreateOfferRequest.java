package com.C2CApp.C2CBackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Date;

public class CreateOfferRequest {
    
    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotBlank(message = "Seller ID is required")
    private String sellerId;

    @NotNull(message = "Price must be specified")
    @Positive(message = "Price must be positive")
    private Double offeredPrice;

    @NotNull(message = "Offer date is required")
    private Date offerDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String token;

    // Default constructor
    public CreateOfferRequest() {
    }

    // Constructor with fields
    public CreateOfferRequest(String productId, String sellerId, Double offeredPrice, Date offerDate, String token) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.offeredPrice = offeredPrice;
        this.offerDate = offerDate;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
