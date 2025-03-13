package com.C2CApp.C2CBackend.entities;

import java.util.Date;

public class CreateOrderDto {
    private String sellerId;
    private String productId;
    private Date orderDate;
    private String paymentMethod;

    public CreateOrderDto() {}

    public CreateOrderDto(String sellerId, String productId, Date orderDate, String paymentMethod) {
        this.sellerId = sellerId;
        this.productId = productId;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}