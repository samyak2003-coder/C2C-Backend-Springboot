package com.C2CApp.C2CBackend.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.C2CApp.C2CBackend.dto.base.ApiResponse;
import com.C2CApp.C2CBackend.dto.user.response.UserResponse;
import com.C2CApp.C2CBackend.dto.user.response.UserProfileResponse;
import com.C2CApp.C2CBackend.dto.user.request.UpdatePasswordRequest;
import com.C2CApp.C2CBackend.dto.user.request.UpdateUserProfileRequest;
import com.C2CApp.C2CBackend.exceptions.BusinessException;
import com.C2CApp.C2CBackend.mapper.UserMapper;
import com.C2CApp.C2CBackend.middleware.JwtAuthenticationMiddleware;
import com.C2CApp.C2CBackend.schema.UserSchema;
import com.C2CApp.C2CBackend.services.UserService;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    private final JwtAuthenticationMiddleware jwtAuth;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(
            UserService userService,
            JwtAuthenticationMiddleware jwtAuth,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtAuth = jwtAuth;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getPublicProfile(
            @PathVariable String userId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            jwtAuth.validateToken(authHeader);
            Optional<UserSchema> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(ApiResponse.success(UserMapper.toProfileResponse(userOpt.get())));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = jwtAuth.validateToken(authHeader).get("id").toString();
            Optional<UserSchema> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(ApiResponse.success(UserMapper.toResponse(userOpt.get())));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/me/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateUserProfileRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = jwtAuth.validateToken(authHeader).get("id").toString();
            Optional<UserSchema> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            UserSchema user = userOpt.get();
            UserMapper.updateEntityFromProfile(user, request);
            userService.updateUser(userId, user);

            return ResponseEntity.ok(ApiResponse.success(UserMapper.toResponse(user)));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = jwtAuth.validateToken(authHeader).get("id").toString();
            Optional<UserSchema> userOpt = userService.getUserById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            UserSchema user = userOpt.get();
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Current password is incorrect"));
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userService.updateUser(userId, user);

            return ResponseEntity.ok(ApiResponse.success(null, "Password updated successfully"));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}
