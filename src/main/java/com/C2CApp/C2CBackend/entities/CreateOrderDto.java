package com.C2CApp.C2CBackend.entities;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CreateOrderDto {
    private String offerId;
    private String sellerId;
    private String productId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;
    
    private String paymentMethod;
    private String token;
    private Double orderPrice;

    public CreateOrderDto() {}

    public CreateOrderDto(String offerId,String sellerId, String productId,Double orderPrice, Date orderDate, String paymentMethod, String token) {
        this.sellerId = sellerId;
        this.offerId = offerId;
        this.productId = productId;
        this.orderPrice = orderPrice;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public String getOfferId(){
        return offerId;
    }

    public void setOfferId(String offerId){
        this.offerId = offerId;
    }


    public Double getOrderPrice(){
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice){
        this.orderPrice = orderPrice;
    }


    public void setToken(String token){
        this.token = token;
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