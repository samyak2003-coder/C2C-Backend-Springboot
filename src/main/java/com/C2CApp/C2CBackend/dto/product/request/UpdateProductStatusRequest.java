package com.C2CApp.C2CBackend.dto.product.request;

import com.C2CApp.C2CBackend.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateProductStatusRequest {
    @NotNull(message = "Status is required")
    private ProductStatus status;

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
