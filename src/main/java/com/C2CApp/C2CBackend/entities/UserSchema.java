package com.C2CApp.C2CBackend.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserSchema {

    @Id
    @Column(nullable = false) 
    private String id;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public UserSchema() {}

    public UserSchema(String name, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}