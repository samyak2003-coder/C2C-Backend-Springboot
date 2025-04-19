package com.C2CApp.C2CBackend.controller.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.C2CApp.C2CBackend.dto.auth.request.SignInRequest;
import com.C2CApp.C2CBackend.dto.auth.request.SignUpRequest;
import com.C2CApp.C2CBackend.dto.auth.response.AuthResponse;
import com.C2CApp.C2CBackend.dto.auth.response.TokenValidationResponse;
import com.C2CApp.C2CBackend.dto.base.ApiResponse;
import com.C2CApp.C2CBackend.mapper.UserMapper;
import com.C2CApp.C2CBackend.schema.UserSchema;
import com.C2CApp.C2CBackend.services.UserService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Dotenv dotenv;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, Dotenv dotenv) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.dotenv = dotenv;
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody SignInRequest request) {
        String email = request.getEmail().toLowerCase();
        UserSchema user = userService.getUserByEmail(email);
        
        if (user == null) {
            return ResponseEntity.status(401)
                .body(ApiResponse.error("User not found"));
        }

        boolean isPasswordValid = userService.checkPassword(email, request.getPassword());
        if (!isPasswordValid) {
            return ResponseEntity.status(401)
                .body(ApiResponse.error("Invalid password"));
        }

        String token = generateToken(user);
        
        AuthResponse response = new AuthResponse(
            token,  // Return token without Bearer prefix
            UserMapper.toResponse(user)
        );

        return ResponseEntity.ok()
            .body(ApiResponse.success(response));
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody SignUpRequest request) {
        String email = request.getEmail().toLowerCase();

        if (userService.getUserByEmail(email) != null) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("User already exists"));
        }

        UserSchema newUser = UserSchema.builder()
            .name(request.getName())
            .email(email)
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();
        userService.createUser(newUser);

        String token = generateToken(newUser);
        
        AuthResponse response = new AuthResponse(
            token,  // Return token without Bearer prefix
            UserMapper.toResponse(newUser)
        );

        return ResponseEntity.ok()
            .body(ApiResponse.success(response));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = null;
        
        // Try to get token from Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null) {
            return ResponseEntity.status(401)
                .body(ApiResponse.error("No token provided"));
        }
        System.out.println("Validating token: " + token);

        try {
            String jwtSecret = dotenv.get("JWT_SECRET");
            if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
                System.out.println("JWT_SECRET is missing or empty");
                return ResponseEntity.status(401)
                    .body(ApiResponse.error("JWT configuration error"));
            }

            Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
            
            System.out.println("Token validated successfully for user: " + claims.get("email"));

            TokenValidationResponse response = new TokenValidationResponse(
                claims.get("id").toString(),
                claims.get("name").toString(),
                claims.get("email").toString(),
                claims.get("role").toString()
            );

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (JwtException e) {
            return ResponseEntity.status(401)
                .body(ApiResponse.error("Invalid token"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok()
            .body(ApiResponse.success(null));
    }

    private String generateToken(UserSchema user) {
        return Jwts.builder()
            .claim("id", user.getId())
            .claim("name", user.getName())
            .claim("email", user.getEmail())
            .claim("role", user.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiry
            .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SECRET"))
            .compact();
    }
}
