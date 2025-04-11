package com.C2CApp.C2CBackend.enums;

public enum ProductStatus {
    AVAILABLE("Available"),
    SOLD("Sold"),
    RESERVED("Reserved"),
    REMOVED("Removed");

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ProductStatus fromString(String text) {
        for (ProductStatus status : ProductStatus.values()) {
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
