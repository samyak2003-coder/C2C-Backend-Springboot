package com.C2CApp.C2CBackend.dto.offer.get;

import java.util.Date;
import com.C2CApp.C2CBackend.enums.OfferStatus;
import com.C2CApp.C2CBackend.enums.ProductStatus;

public class GetOfferResponse {
    private String offerId;
    private String buyerId;
    private String sellerId;
    private String productId;
    private String productTitle;
    private Double price;
    private Date createdDate;
    private OfferStatus offerStatus;
    private ProductStatus productStatus;

    // Constructors
    public GetOfferResponse() {}

    public GetOfferResponse(String offerId, String buyerId, String sellerId, 
                          String productId, String productTitle, Double price, 
                          Date createdDate, OfferStatus offerStatus, 
                          ProductStatus productStatus) {
        this.offerId = offerId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.productTitle = productTitle;
        this.price = price;
        this.createdDate = createdDate;
        this.offerStatus = offerStatus;
        this.productStatus = productStatus;
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

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
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

    public OfferStatus getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(OfferStatus offerStatus) {
        this.offerStatus = offerStatus;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
