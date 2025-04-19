package com.C2CApp.C2CBackend.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.C2CApp.C2CBackend.exceptions.JwtAuthenticationException;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationMiddleware {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationMiddleware.class);
    private final Dotenv dotenv;
    
    @Autowired
    public JwtAuthenticationMiddleware(Dotenv dotenv) {
        this.dotenv = dotenv;
    }
    
    public Claims validateToken(String authHeader) throws JwtAuthenticationException {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new JwtAuthenticationException("Invalid authorization header");
            }
            
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            String jwtSecret = dotenv.get("JWT_SECRET");
            if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
                logger.error("JWT_SECRET is not configured in environment");
                throw new JwtAuthenticationException("JWT configuration error");
            }

            logger.debug("Attempting to validate token with length: {}", token.length());
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)  // Use the same secret instance
                    .parseClaimsJws(token)
                    .getBody();
            
            if (claims.get("id") == null || claims.get("role") == null) {
                logger.error("Token missing required claims. Claims present: {}", claims.keySet());
                throw new JwtAuthenticationException("Missing required claims in token");
            }

            logger.debug("Token validated successfully. User ID: {}, Role: {}", 
                      claims.get("id"), claims.get("role"));
            return claims;
        } catch (Exception e) {
            logger.error("Token validation failed. Error: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid authentication token: " + e.getMessage());
        }
    }
}
