package com.C2CApp.C2CBackend.controller;

import com.C2CApp.C2CBackend.entities.ProductSchema;
import com.C2CApp.C2CBackend.entities.SellProductInput;
import com.C2CApp.C2CBackend.entities.SellProductResponse;
import com.C2CApp.C2CBackend.entities.UserSchema;
import com.C2CApp.C2CBackend.repositories.ProductRepository;
import com.C2CApp.C2CBackend.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import io.jsonwebtoken.Jwts;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Product {

    private final Dotenv dotenv;
    private final UserService userService;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public Product(ProductService productService, ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.productService = productService;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<SellProductResponse> makeOffer(@RequestHeader("Authorization") String token,@RequestBody SellProductInput sellProductInput) {

        if (token == null) {
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

        final String sellerId = claims.get("id").toString();

        Optional<UserSchema> user = userService.getUserById(sellerId);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String title = sellProductInput.getTitle();
        String description = sellProductInput.getTitle();
        double price = sellProductInput.getPrice();
        String category = sellProductInput.getCategory();
        String productCondition = sellProductInput.getProductCondition();
        String status = sellProductInput.getStatus();

        ProductSchema newProduct = new ProductSchema(title,description,price,category,productCondition,sellerId,status);
        productService.createProduct(newProduct);
        
        return ResponseEntity.ok(new SellProductResponse(title,description,price,category,productCondition,status));
    }

    @RequestMapping(
        value = "/get",
        method = RequestMethod.GET,
        produces = "application/json")
public ResponseEntity<List<ProductSchema>> getProduct(@RequestHeader("Authorization") String token) {

    if (token == null) {
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

    final String userId = claims.get("id").toString();

    Optional<UserSchema> user = userService.getUserById(userId);

    if (user.isEmpty()) {
        return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok(productService.getProducts());
}
}
