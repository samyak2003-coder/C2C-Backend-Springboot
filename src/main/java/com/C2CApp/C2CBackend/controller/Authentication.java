package com.C2CApp.C2CBackend.controller;

import com.C2CApp.C2CBackend.entities.*;
import com.C2CApp.C2CBackend.repositories.UserRepository;
import com.C2CApp.C2CBackend.services.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.validator.routines.EmailValidator;

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

    @RequestMapping(value = "/hello",
            method = RequestMethod.GET,
            produces = "text/plain")
    public String hello() {
        return "Hello, World!";
    }

    private boolean isEmailPatternNotValid(String email) {
        return !EmailValidator.getInstance().isValid(email);
    }

    @RequestMapping(
            value = "/sign-in",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<SignInAndSignUpResponse> login(@RequestBody final SignInDetails signInDetails) {
        String email = signInDetails.getEmail();
        String password = signInDetails.getPassword();
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
            return ResponseEntity.ok(new SignInAndSignUpResponse(token, "Success", userName, userEmail));
        } else {
            return ResponseEntity.status(401).body(new SignInAndSignUpResponse("", "Invalid password", "", ""));
        }
    }

    @RequestMapping(
            value = "/sign-up",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<SignInAndSignUpResponse> register(@RequestBody final SignUpDetails signUpDetails) {
        String email = signUpDetails.getEmail();
        String name = signUpDetails.getName();
        String password = signUpDetails.getPassword();
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
        if (userService.getUserByEmail(email) != null) {
            return ResponseEntity.status(409).body(new SignInAndSignUpResponse("", "User already exists", "", ""));
        }
        UserSchema newUser = new UserSchema(name, email, password);
        userService.createUser(newUser);
        String token = Jwts.builder()
                .claim("id", newUser.getId())
                .claim("name", name)
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SECRET"))
                .compact();
        return ResponseEntity.ok(new SignInAndSignUpResponse(token, "Success", name, email));
    }

    @RequestMapping(
        value = "/getInfo",
        method = RequestMethod.GET,  
        produces = "application/json")
public ResponseEntity<List<String>> getInfo(@RequestHeader("Authorization") String token) {

    if (token == null || token.isEmpty()) {
        return ResponseEntity.badRequest().build();
    }

    Claims claims;

    try {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

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

    List<String> UserInfo = new ArrayList<>();
    UserInfo.add(name);
    UserInfo.add(email);

    return ResponseEntity.ok(UserInfo);
}

}