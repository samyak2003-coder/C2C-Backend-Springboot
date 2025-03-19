package com.C2CApp.C2CBackend.controller;

import com.C2CApp.C2CBackend.entities.*;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.OrderService;
import com.C2CApp.C2CBackend.services.ProductService;
import com.C2CApp.C2CBackend.services.UserService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;
    private final OfferService offerService;
    private final OrderService orderService;
    private final ProductService productService;
    private final Dotenv dotenv;

    @Autowired
    public AdminController(UserService userService, OfferService offerService, OrderService orderService, ProductService productService, Dotenv dotenv) {
        this.userService = userService;
        this.offerService = offerService;
        this.orderService = orderService;
        this.productService = productService;
        this.dotenv = dotenv;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserSchema>> getUsers(HttpServletRequest request) {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/getOffers")
    public ResponseEntity<List<OfferSchema>> getOffers(HttpServletRequest request) {

        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/getOrders")
    public ResponseEntity<List<OrderSchema>> getOrders(HttpServletRequest request) {

        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductSchema>> getProducts(HttpServletRequest request) {
        return ResponseEntity.ok(productService.getProducts());
    }
}