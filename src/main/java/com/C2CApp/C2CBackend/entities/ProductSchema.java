package com.C2CApp.C2CBackend.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductSchema {
    
    @Id
    @Column(nullable = false)
    private String productId;

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

    @Column(nullable = false)
    private String status = "Unsold";

    public ProductSchema() {
        this.productId = UUID.randomUUID().toString();
    }

    public ProductSchema(String title, String description, double price, String category, String productCondition, String sellerId, String status) {
        this.productId = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.productCondition = productCondition; 
        this.sellerId = sellerId;
        this.status = status;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public void setProductCondition(String productCondition) { // Fixed setter parameter
        this.productCondition = productCondition;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}