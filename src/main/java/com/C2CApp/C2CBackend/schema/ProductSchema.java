package com.C2CApp.C2CBackend.schema;

import com.C2CApp.C2CBackend.enums.ProductStatus;
import jakarta.persistence.*;
import java.util.UUID;
import java.util.Date;

@Entity
@Table(name = "products")
public class ProductSchema {
    
    @Id
    @Column(name = "product_id", nullable = false)
    private String id;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String productCondition;

    @Column(nullable = false)
    private String sellerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    // public constructor for JPA
    public ProductSchema() {
        this.id = UUID.randomUUID().toString();
        this.status = ProductStatus.AVAILABLE;
        this.createdDate = new Date();
    }

    // Builder class
    public static class Builder {
        private String title;
        private String description;
        private double price;
        private String category;
        private String productCondition;
        private String sellerId;
        private ProductStatus status = ProductStatus.AVAILABLE; // Default

        public Builder title(String title) { 
            this.title = title; 
            return this; 
        }

        public Builder description(String description) { 
            this.description = description; 
            return this; 
        }

        public Builder price(double price) { 
            this.price = price; 
            return this; 
        }

        public Builder category(String category) { 
            this.category = category; 
            return this; 
        }

        public Builder productCondition(String condition) { 
            this.productCondition = condition; 
            return this; 
        }

        public Builder sellerId(String sellerId) { 
            this.sellerId = sellerId; 
            return this; 
        }

        public Builder status(ProductStatus status) { 
            this.status = status; 
            return this; 
        }

        public ProductSchema build() {
            validateFields();
            ProductSchema product = new ProductSchema();
            product.setTitle(this.title);
            product.setDescription(this.description);
            product.setPrice(this.price);
            product.setCategory(this.category);
            product.setProductCondition(this.productCondition);
            product.setSellerId(this.sellerId);
            product.setStatus(this.status);
            return product;
        }

        private void validateFields() {
            if (title == null || title.isBlank()) 
                throw new IllegalStateException("Title is required");
            if (description == null || description.isBlank()) 
                throw new IllegalStateException("Description is required");
            if (price <= 0) 
                throw new IllegalStateException("Price must be positive");
            if (category == null || category.isBlank()) 
                throw new IllegalStateException("Category is required");
            if (productCondition == null || productCondition.isBlank()) 
                throw new IllegalStateException("Product condition is required");
            if (sellerId == null || sellerId.isBlank()) 
                throw new IllegalStateException("Seller ID is required");
        }
    }

    // Static method to create a new Builder instance
    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters needed by JPA
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    // Business methods
    public void markAsSold() {
        this.status = ProductStatus.SOLD;
    }

    public void markAsReserved() {
        this.status = ProductStatus.RESERVED;
    }

    public void markAsAvailable() {
        this.status = ProductStatus.AVAILABLE;
    }

    public void remove() {
        this.status = ProductStatus.REMOVED;
    }

    public boolean isAvailable() {
        return this.status == ProductStatus.AVAILABLE;
    }

    public boolean isSold() {
        return this.status == ProductStatus.SOLD;
    }

    // For backwards compatibility with string-based status
    public String getStatusString() {
        return status.toString();
    }

    public void setStatusString(String statusStr) {
        this.status = ProductStatus.fromString(statusStr);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
