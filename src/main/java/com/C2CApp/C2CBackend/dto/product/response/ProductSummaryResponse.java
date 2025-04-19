package com.C2CApp.C2CBackend.dto.product.response;

import com.C2CApp.C2CBackend.enums.ProductStatus;
import java.util.Date;
import java.util.List;
import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;

public class ProductSummaryResponse {
    private String id;
    private String title;
    private String description;
    private double price;
    private String category;
    private ProductStatus status;
    private String sellerId;
    private Date createdDate;
    private List<GetOfferResponse> offers;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<GetOfferResponse> getOffers() {
        return offers;
    }

    public void setOffers(List<GetOfferResponse> offers) {
        this.offers = offers;
    }
}
