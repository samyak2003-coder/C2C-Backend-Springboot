package com.C2CApp.C2CBackend.entities;

public class DeleteAdminDetails {
    private String entityId;

    public DeleteAdminDetails() {}

    public DeleteAdminDetails(String entityId){
        this.entityId = entityId;
    }

    public String getEntityId(){
        return entityId;
    }

    public void setEntityId(String entityId){
        this.entityId = entityId;
    }

}
