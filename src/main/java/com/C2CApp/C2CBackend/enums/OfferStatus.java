package com.C2CApp.C2CBackend.enums;

public enum OfferStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");

    private final String status;

    OfferStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static OfferStatus fromString(String text) {
        for (OfferStatus status : OfferStatus.values()) {
            if (status.status.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return status;
    }
}
