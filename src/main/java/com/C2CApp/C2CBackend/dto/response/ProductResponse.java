package com.C2CApp.C2CBackend.dto.response;

import com.C2CApp.C2CBackend.domain.Product;
import com.C2CApp.C2CBackend.enums.ProductStatus;

public class ProductResponse {
    private String id;
    private String title;
    private String description;
    private Double price;
    private String category;
    private String condition;
    private String sellerId;
    private ProductStatus status;

    // Default constructor
    public ProductResponse() {
    }

    // Constructor from domain object
    public ProductResponse(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.condition = product.getCondition();
        this.sellerId = product.getSellerId();
        this.status = product.getStatus();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    public String getSellerId() {
        return sellerId;
    }

    public ProductStatus getStatus() {
        return status;
    }

    // Helper methods
    public boolean isAvailable() {
        return status == ProductStatus.AVAILABLE;
    }

    public boolean isSold() {
        return status == ProductStatus.SOLD;
    }

    public boolean isReserved() {
        return status == ProductStatus.RESERVED;
    }

    // For backwards compatibility with string-based status
    public String getStatusString() {
        return status.toString();
    }
}
