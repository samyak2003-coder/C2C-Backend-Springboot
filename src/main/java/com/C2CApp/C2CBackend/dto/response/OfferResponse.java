package com.C2CApp.C2CBackend.dto.response;

import com.C2CApp.C2CBackend.domain.Offer;
import com.C2CApp.C2CBackend.domain.Product;
import com.C2CApp.C2CBackend.enums.OfferStatus;
import com.C2CApp.C2CBackend.enums.ProductStatus;
import java.util.Date;

public class OfferResponse {
    private String id;
    private String buyerId;
    private String sellerId;
    private String productId;
    private String productTitle;
    private Double offeredPrice;
    private Date createdDate;
    private OfferStatus offerStatus;
    private ProductStatus productStatus;

    // Default constructor
    public OfferResponse() {
    }

    // Constructor from domain objects
    public OfferResponse(Offer offer, Product product) {
        this.id = offer.getId();
        this.buyerId = offer.getBuyerId();
        this.sellerId = offer.getSellerId();
        this.productId = offer.getProductId();
        this.offeredPrice = offer.getPrice();
        this.createdDate = offer.getCreatedDate();
        this.offerStatus = offer.getStatus();
        
        if (product != null) {
            this.productTitle = product.getTitle();
            this.productStatus = product.getStatus();
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public Double getOfferedPrice() {
        return offeredPrice;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public OfferStatus getOfferStatus() {
        return offerStatus;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    // Helper methods
    public boolean isOfferPending() {
        return offerStatus == OfferStatus.PENDING;
    }

    public boolean isProductAvailable() {
        return productStatus == ProductStatus.AVAILABLE;
    }
}
