package com.C2CApp.C2CBackend.dto.admin.request;

public class DeleteAdminRequest {
    private String entityId;
    private String entityType;

    public DeleteAdminRequest() {}

    public DeleteAdminRequest(String entityId, String entityType) {
        this.entityId = entityId;
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
