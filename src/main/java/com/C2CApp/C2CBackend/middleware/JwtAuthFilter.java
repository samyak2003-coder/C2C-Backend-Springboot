package com.C2CApp.C2CBackend.middleware;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtAuthenticationMiddleware jwtAuthMiddleware;
    
    public JwtAuthFilter(JwtAuthenticationMiddleware jwtAuthMiddleware) {
        this.jwtAuthMiddleware = jwtAuthMiddleware;
    }
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        // Try to extract and validate token
        String token = extractToken(request);
        logger.debug("Extracted token from request: {}", token != null ? "present" : "null");
        if (token != null) {
            try {
                Claims claims = jwtAuthMiddleware.validateToken(token);
                if (claims != null) {
                    logger.debug("Token validated successfully for user: {}", claims.get("email"));
                    String role = String.valueOf(claims.get("role"));
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        String.valueOf(claims.get("email")),
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                    );
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                logger.error("Token validation failed: ", e);
                // Only clear context when token validation explicitly fails
                SecurityContextHolder.clearContext();
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", authHeader != null ? "present" : "null");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
