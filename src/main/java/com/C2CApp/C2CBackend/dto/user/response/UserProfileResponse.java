package com.C2CApp.C2CBackend.dto.user.response;

public class UserProfileResponse {
    private String id;
    private String name;
    
    // Constructors
    public UserProfileResponse() {}
    
    public UserProfileResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
