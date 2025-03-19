package com.C2CApp.C2CBackend.controller;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.C2CApp.C2CBackend.entities.DeleteAdminDetails;
import com.C2CApp.C2CBackend.entities.SignInDetails;
import com.C2CApp.C2CBackend.entities.SignUpDetails;
import com.C2CApp.C2CBackend.entities.UserSchema;
import com.C2CApp.C2CBackend.services.UserService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private static final String LOGIN_VIEW = "signin";  
    private static final String SIGNUP_VIEW = "signup";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Dotenv dotenv;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, Dotenv dotenv) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.dotenv = dotenv;
    }

    private boolean isEmailPatternNotValid(String email) {
        return !EmailValidator.getInstance().isValid(email);
    }

    @PostMapping("/signin")
    public String login(
        @Valid @ModelAttribute("signInDetails") SignInDetails form,
        BindingResult bindingResult,
        HttpSession session,
        HttpServletResponse response) {
        
        if (bindingResult.hasErrors()) {
            session.setAttribute("AuthStatus", "FAILED");
            return LOGIN_VIEW;
        }

        String email = form.getEmail().toLowerCase();
        String password = form.getPassword();

        if (email.isEmpty() || isEmailPatternNotValid(email) || password.isEmpty()) {
            session.setAttribute("AuthStatus", "FAILED");
            return LOGIN_VIEW;
        }

        UserSchema user = userService.getUserByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("AuthStatus", "FAILED");
            return LOGIN_VIEW;
        }
        session.setAttribute("AuthStatus", "OK");

        String token = Jwts.builder()
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SECRET"))
                .compact();

        // Set token in a cookie
        Cookie tokenCookie = new Cookie("auth_token", token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(true);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(3600);
        response.addCookie(tokenCookie);

        return "home"; 
    }

    @PostMapping("/signup")
    public String register(
        @Valid @ModelAttribute("signUpDetails") SignUpDetails form,
        BindingResult bindingResult,
        HttpSession session,
        HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            session.setAttribute("AuthStatus", "FAILED");
            return SIGNUP_VIEW;
        }

        String email = form.getEmail().toLowerCase();
        String password = form.getPassword();
        String name = form.getName();

        // Check if the user already exists
        if (userService.getUserByEmail(email) != null) {
            session.setAttribute("AuthStatus", "USER_EXISTS");
            return SIGNUP_VIEW;
        }

        if (email.isEmpty() || isEmailPatternNotValid(email) || password.isEmpty() || name.isEmpty()) {
            session.setAttribute("AuthStatus", "FAILED");
            return SIGNUP_VIEW;
        }

        // Create new user
        UserSchema newUser = new UserSchema(name, email, password);
        userService.createUser(newUser);

        // Generate JWT token
        String token = Jwts.builder()
                .claim("id", newUser.getId())
                .claim("name", name)
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SECRET"))
                .compact();

        // Set token in a cookie
        Cookie tokenCookie = new Cookie("auth_token", token);
        tokenCookie.setHttpOnly(true);  
        tokenCookie.setSecure(true);    
        tokenCookie.setPath("/");      
        tokenCookie.setMaxAge(3600);    
        response.addCookie(tokenCookie);

        return "home";
    }

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
    
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
    
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body("");
        }
    
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return ResponseEntity.status(401).body("");
        }
    
        String name = claims.get("name").toString();
        return ResponseEntity.ok(name);
    }
    
    @PostMapping("/delete-user")
    public String deleteUser(@Valid @ModelAttribute("deleteEntityDetails") DeleteAdminDetails form,
        BindingResult bindingResult,
        HttpSession session,
        HttpServletResponse response){
            String userId = form.getEntityId();
            
            userService.deleteUser(userId);
            return "admin";
    }

}