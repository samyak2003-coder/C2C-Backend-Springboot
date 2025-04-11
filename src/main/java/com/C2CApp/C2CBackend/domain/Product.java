package com.C2CApp.C2CBackend.domain;

import com.C2CApp.C2CBackend.enums.ProductStatus;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private String sellerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    // Default constructor
    public Product() {
        this.id = UUID.randomUUID().toString();
        this.status = ProductStatus.AVAILABLE;
    }

    // Constructor with fields
    public Product(String title, String description, double price, String category, 
                  String condition, String sellerId) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.condition = condition;
        this.sellerId = sellerId;
        this.status = ProductStatus.AVAILABLE;
    }

    // Getters and Setters
    public String getId() {
        return id;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
}
