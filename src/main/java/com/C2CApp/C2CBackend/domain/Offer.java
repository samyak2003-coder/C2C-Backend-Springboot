package com.C2CApp.C2CBackend.domain;

import org.springframework.format.annotation.DateTimeFormat;
import com.C2CApp.C2CBackend.enums.OfferStatus;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "offers")
public class Offer {
    
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String buyerId;

    @Column(nullable = false)
    private String sellerId;

    @Column(nullable = false)
    private Double price;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status;

    // Default constructor
    public Offer() {
        this.id = UUID.randomUUID().toString();
        this.status = OfferStatus.PENDING;
    }

    // Constructor with fields
    public Offer(String buyerId, String sellerId, String productId, Double price, Date createdDate) {
        this.id = UUID.randomUUID().toString();
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.price = price;
        this.createdDate = createdDate;
        this.status = OfferStatus.PENDING;
    }

    // Getters and Setters with improved naming
    public String getId() {
        return id;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    // Business methods
    public void accept() {
        this.status = OfferStatus.ACCEPTED;
    }

    public void reject() {
        this.status = OfferStatus.REJECTED;
    }

    public void cancel() {
        this.status = OfferStatus.CANCELLED;
    }

    public boolean isPending() {
        return this.status == OfferStatus.PENDING;
    }

    public boolean isAccepted() {
        return this.status == OfferStatus.ACCEPTED;
    }

    // For backwards compatibility with string-based status
    public String getStatusString() {
        return status.toString();
    }

    public void setStatusString(String statusStr) {
        this.status = OfferStatus.fromString(statusStr);
    }
}
