package com.C2CApp.C2CBackend.schema;

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

    @Column(nullable = false)
    private String role;

    // Private constructor to enforce use of Builder
    private UserSchema() {
        this.id = UUID.randomUUID().toString();
        this.role = "USER"; // Default role
    }

    // Builder class
    public static class Builder {
        private String name;
        private String email;
        private String password;
        private String role = "USER"; // Default role

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public UserSchema build() {
            validateFields();
            UserSchema user = new UserSchema();
            user.name = this.name;
            user.email = this.email;
            user.password = this.password;
            user.role = this.role;
            return user;
        }

        private void validateFields() {
            if (password == null || password.isBlank())
                throw new IllegalStateException("Password is required");
            if (role == null || role.isBlank())
                throw new IllegalStateException("Role is required");
            // Email validation if provided
            if (email != null && !email.isBlank() && !email.contains("@"))
                throw new IllegalStateException("Invalid email format");
        }
    }

    // Static method to create a new Builder instance
    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("Password cannot be empty");
        }
        this.password = password;
    }

    public void setRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalStateException("Role cannot be empty");
        }
        this.role = role;
    }
}
