package com.C2CApp.C2CBackend.dto.offer.update;

import jakarta.validation.constraints.NotNull;
import com.C2CApp.C2CBackend.enums.OfferStatus;

public class UpdateOfferRequest {
    @NotNull
    private String offerId;

    @NotNull
    private OfferStatus status;

    // Constructors
    public UpdateOfferRequest() {}

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
}
