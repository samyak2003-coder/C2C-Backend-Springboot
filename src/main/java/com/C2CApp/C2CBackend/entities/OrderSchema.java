package com.C2CApp.C2CBackend.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderSchema {
    
    @Id
    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String buyerId;

    @Column(nullable = false)
    private String sellerId;

    @Column(nullable = false)
    private String productId;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date orderDate;

    @Column(nullable = false)
    private String paymentMethod;

    public OrderSchema() {
        this.orderId = UUID.randomUUID().toString();
    }

    public OrderSchema(String buyerId, String sellerId, String productId, Date orderDate, String paymentMethod) {
        this.orderId = UUID.randomUUID().toString();
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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