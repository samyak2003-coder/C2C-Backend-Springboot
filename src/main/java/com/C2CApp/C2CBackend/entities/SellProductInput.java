package com.C2CApp.C2CBackend.entities;

import jakarta.persistence.Column;

public class SellProductInput {
    String token;
    private String title;
    private String description;
    private double price;
    private String category;
    private String productCondition;
    
    @Column(nullable = false)
    private String status = "Unsold"; 

    public SellProductInput() {}

    public SellProductInput(String title, String description, double price, String category, 
                       String productCondition, String sellerId,String token, String status) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.productCondition = productCondition;
        this.status = status;
        this.token = token;
    }

    // Getters and Setters
    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}