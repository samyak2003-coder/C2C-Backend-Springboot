package com.C2CApp.C2CBackend.entities;

public class SellProductResponse {
    private String title;
    private String description;
    private double price;
    private String category;
    private String condition;
    private String status;

    public SellProductResponse() {}

    public SellProductResponse(String title, String description, double price, String category, 
        String condition,String status) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.condition =condition;
        this.status = status;
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
        return condition;
    }

    public void setProductCondition(String condition) {
        this.condition = condition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
