package com.C2CApp.C2CBackend.controller;

import com.C2CApp.C2CBackend.entities.*;
import com.C2CApp.C2CBackend.repositories.UserRepository;
import com.C2CApp.C2CBackend.services.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Authentication {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Dotenv dotenv;
    private final UserRepository userRepository;

    @Autowired
    public Authentication(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "text/plain")
    public String hello() {
        return "Hello, World!";
    }

    private boolean isEmailPatternNotValid(String email) {
        return !EmailValidator.getInstance().isValid(email);
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity<SignInAndSignUpResponse> login(
        @RequestParam String email, 
        @RequestParam String password, 
        HttpServletResponse response){
    
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid email", "", ""));
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid password", "", ""));
        }
    
        email = email.toLowerCase();
        UserSchema user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(new SignInAndSignUpResponse("", "User not found", "", ""));
        }
    
        String userName = user.getName();
        String userEmail = user.getEmail();
    
        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = Jwts.builder()
                    .claim("id", user.getId())
                    .claim("name", userName)
                    .claim("email", userEmail)
                    .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SECRET"))
                    .compact();
    
            // Set token in a cookie
            Cookie tokenCookie = new Cookie("auth_token", token);
            tokenCookie.setHttpOnly(true);  // Prevent JavaScript access
            tokenCookie.setSecure(true);    // Only send over HTTPS
            tokenCookie.setPath("/");       // Available site-wide
            tokenCookie.setMaxAge(3600);    // Expires in 1 hour
            response.addCookie(tokenCookie);
    
            return ResponseEntity.ok(new SignInAndSignUpResponse(token, "Success", userName, userEmail));
        } else {
            return ResponseEntity.status(401).body(new SignInAndSignUpResponse("", "Invalid password", "", ""));
        }
    }
    

    @RequestMapping(
        value = "/sign-up",
        method = RequestMethod.POST,
        consumes = "application/x-www-form-urlencoded", // Accept form data
        produces = "application/json")
    public ResponseEntity<SignInAndSignUpResponse> register(
        @RequestParam String name, 
        @RequestParam String email, 
        @RequestParam String password, 
        HttpServletResponse response) {

        // Validate input
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid email", "", ""));
        }
        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid name", "", ""));
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid password", "", ""));
        }

        email = email.toLowerCase();

        // Check if the user already exists
        if (userService.getUserByEmail(email) != null) {
            return ResponseEntity.status(409).body(new SignInAndSignUpResponse("", "User already exists", "", ""));
        }

        // Create new user
        UserSchema newUser = new UserSchema(name, email, passwordEncoder.encode(password)); // Make sure to hash the password
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
        tokenCookie.setHttpOnly(true);  // Prevent JavaScript access
        tokenCookie.setSecure(true);    // Only send over HTTPS
        tokenCookie.setPath("/");       // Available site-wide
        tokenCookie.setMaxAge(3600);    // Expires in 1 hour
        response.addCookie(tokenCookie);

        return ResponseEntity.ok(new SignInAndSignUpResponse(token, "Success", name, email));
    }

    @RequestMapping(
        value = "/getInfo",
        method = RequestMethod.GET,  
        produces = "application/json")
    public ResponseEntity<List<String>> getInfo(HttpServletRequest request) {
        // Retrieve JWT token from cookies
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
            return ResponseEntity.badRequest().build();
        }

        Claims claims;

        try {
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return ResponseEntity.badRequest().build();
        }

        final String id = claims.get("id").toString();
        final String name = claims.get("name").toString();
        final String email = claims.get("email").toString();

        Optional<UserSchema> user = userService.getUserById(id);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        UserSchema userSchema = user.get();

        if (!userSchema.getEmail().equals(email) || !userSchema.getName().equals(name)) {
            return ResponseEntity.badRequest().build();
        }

        List<String> userInfo = new ArrayList<>();
        userInfo.add(name);
        userInfo.add(email);

        return ResponseEntity.ok(userInfo);
    }
}