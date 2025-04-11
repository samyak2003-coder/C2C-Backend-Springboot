package com.C2CApp.C2CBackend.controller;

import com.C2CApp.C2CBackend.entities.*;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.schema.UserSchema;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.ProductService;
import com.C2CApp.C2CBackend.services.UserService;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;
    private final OfferService offerService;
    private final ProductService productService;

    @Autowired
    public AdminController(UserService userService, OfferService offerService, ProductService productService, Dotenv dotenv) {
        this.userService = userService;
        this.offerService = offerService;
        this.productService = productService;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserSchema>> getUsers(HttpServletRequest request) {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/getOffers")
    public ResponseEntity<List<OfferSchema>> getOffers(HttpServletRequest request) {

        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductSchema>> getProducts(HttpServletRequest request) {
        return ResponseEntity.ok(productService.getProducts());
    }
}