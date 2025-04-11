package com.C2CApp.C2CBackend.dto.request;

import com.C2CApp.C2CBackend.enums.OfferStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateOfferRequest {
    
    @NotBlank(message = "Offer ID is required")
    private String offerId;

    @NotNull(message = "Status is required")
    private OfferStatus status;

    // Default constructor
    public UpdateOfferRequest() {
    }

    // Constructor with fields
    public UpdateOfferRequest(String offerId, OfferStatus status) {
        this.offerId = offerId;
        this.status = status;
    }

    // Getters and Setters
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    // For backwards compatibility with string-based status
    public String getStatusString() {
        return status.toString();
    }

    public void setStatusString(String statusStr) {
        this.status = OfferStatus.fromString(statusStr);
    }
}
