package com.C2CApp.C2CBackend.mapper;

import com.C2CApp.C2CBackend.dto.auth.request.SignUpRequest;
import com.C2CApp.C2CBackend.dto.auth.response.TokenValidationResponse;
import com.C2CApp.C2CBackend.dto.user.response.UserResponse;
import com.C2CApp.C2CBackend.dto.user.request.UpdateUserProfileRequest;
import com.C2CApp.C2CBackend.dto.user.response.UserProfileResponse;
import com.C2CApp.C2CBackend.schema.UserSchema;

public class UserMapper {

    private UserMapper() {
        // Private constructor to prevent instantiation
    }

    public static UserResponse toResponse(UserSchema user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }

    public static UserProfileResponse toProfileResponse(UserSchema user) {
        return new UserProfileResponse(
            user.getId(),
            user.getName()
        );
    }

    public static TokenValidationResponse toTokenValidationResponse(UserSchema user) {
        TokenValidationResponse response = new TokenValidationResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }

    public static UserSchema toEntity(SignUpRequest request) {
        return UserSchema.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(request.getPassword())
            .build();
    }

    public static void updateEntityFromProfile(UserSchema user, UpdateUserProfileRequest request) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
    }
}
