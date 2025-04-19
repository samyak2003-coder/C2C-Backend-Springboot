package com.C2CApp.C2CBackend.schema;

import com.C2CApp.C2CBackend.enums.OfferStatus;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "offers")
public class OfferSchema {
    
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status;

    // Protected constructor for JPA
    protected OfferSchema() {
        this.id = UUID.randomUUID().toString();
        this.status = OfferStatus.PENDING;
        this.createdDate = new Date();
    }

    // Builder class
    public static class Builder {
        private String productId;
        private String buyerId;
        private String sellerId;
        private Double price;
        private OfferStatus status = OfferStatus.PENDING; // Default
        private Date createdDate = new Date(); // Default to current date

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder buyerId(String buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        public Builder sellerId(String sellerId) {
            this.sellerId = sellerId;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder status(OfferStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdDate(Date createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public OfferSchema build() {
            validateFields();
            OfferSchema offer = new OfferSchema();
            offer.setProductId(this.productId);
            offer.setBuyerId(this.buyerId);
            offer.setSellerId(this.sellerId);
            offer.setPrice(this.price);
            offer.setStatus(this.status);
            offer.setCreatedDate(this.createdDate);
            return offer;
        }

        private void validateFields() {
            if (productId == null || productId.isBlank())
                throw new IllegalStateException("Product ID is required");
            if (buyerId == null || buyerId.isBlank())
                throw new IllegalStateException("Buyer ID is required");
            if (sellerId == null || sellerId.isBlank())
                throw new IllegalStateException("Seller ID is required");
            if (price == null || price <= 0)
                throw new IllegalStateException("Price must be positive");
        }
    }

    // Static method to create a new Builder instance
    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public Double getPrice() {
        return price;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public OfferStatus getStatus() {
        return status;
    }

    // Setters needed by JPA and Builder
    protected void setId(String id) {
        this.id = id;
    }

    protected void setProductId(String productId) {
        this.productId = productId;
    }

    protected void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    protected void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    protected void setPrice(Double price) {
        this.price = price;
    }

    protected void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
