package com.C2CApp.C2CBackend.entities;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "offers")
public class OfferSchema {
    
    @Id
    @Column(nullable = false)
    private String offerId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String buyerId;

    @Column(nullable = false)
    private String sellerId;

    @Column(nullable = false)
    private Double offeredPrice;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date offerDate;

    @Column(nullable = false)
    private String status = "Not accepted";

    public OfferSchema() {
        this.offerId = UUID.randomUUID().toString();
    }

    public OfferSchema(String buyerId,Date offerDate,Double offeredPrice, String productId, String status, String sellerId) {
        this.offerId = UUID.randomUUID().toString();
        this.buyerId = buyerId;
        this.offerDate = offerDate;
        this.offeredPrice = offeredPrice;
        this.productId = productId;
        this.status =status;
        this.sellerId = sellerId;
    }

    // Getters and Setters
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public Double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(Double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}