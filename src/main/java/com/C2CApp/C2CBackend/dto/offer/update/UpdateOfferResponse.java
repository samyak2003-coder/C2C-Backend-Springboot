package com.C2CApp.C2CBackend.dto.offer.update;

import com.C2CApp.C2CBackend.enums.OfferStatus;

public class UpdateOfferResponse {
    private String offerId;
    private OfferStatus status;
    private String message;

    // Constructors
    public UpdateOfferResponse() {}

    public UpdateOfferResponse(String offerId, OfferStatus status, String message) {
        this.offerId = offerId;
        this.status = status;
        this.message = message;
    }

    // Static factory methods for common responses
    public static UpdateOfferResponse success(String offerId, OfferStatus status) {
        return new UpdateOfferResponse(offerId, status, "Offer updated successfully");
    }

    public static UpdateOfferResponse notFound(String offerId) {
        return new UpdateOfferResponse(offerId, null, "Offer not found");
    }

    public static UpdateOfferResponse invalidStatus(String offerId) {
        return new UpdateOfferResponse(offerId, null, "Invalid offer status");
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
