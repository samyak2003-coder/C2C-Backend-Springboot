package com.C2CApp.C2CBackend.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.C2CApp.C2CBackend.entities.CreateOrderDto;
import com.C2CApp.C2CBackend.entities.OrderSchema;
import com.C2CApp.C2CBackend.entities.UserSchema;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.OrderService;
import com.C2CApp.C2CBackend.services.UserService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final Dotenv dotenv;
    private final OfferService offerService;

    @Autowired
    public OrderController(OfferService offerService, UserService userService,Dotenv dotenv,OrderService orderService){
        this.userService = userService;
        this.offerService = offerService;
        this.dotenv = dotenv;
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public String create(
        @Valid @ModelAttribute("createOrderDetails") CreateOrderDto form,
        BindingResult bindingResult,
        HttpSession session,
        HttpServletResponse response
    ){
        if (bindingResult.hasErrors()) {
            session.setAttribute("OrderStatus", "FAILED");
        }

        String token = form.getToken();
        String productId = form.getProductId();
        Date orderDate = form.getOrderDate();
        String paymentMethod = form.getPaymentMethod();
        String offerId = form.getOfferId();
        String sellerId = form.getSellerId();
        Double orderPrice = form.getOrderPrice();

        Claims claims;

        try {
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            session.setAttribute("OrderStatus","FAILED");
            return "products";
        }

        final String buyerId = claims.get("id").toString();

        Optional<UserSchema> user = userService.getUserById(buyerId);

        if (user.isEmpty()) {
            session.setAttribute("OrderStatus","FAILED");
            return "home";
        }

        OrderSchema newOrder = new OrderSchema(buyerId, sellerId, productId,orderPrice, orderDate, paymentMethod);
        orderService.createOrder(newOrder);
        offerService.deleteOfferById(offerId);
        return "offers";
    }

    @GetMapping("get-orders")
    public ResponseEntity<?> getByUserId(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String token = null;

        // Retrieve auth_token from cookies
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // If token is missing or empty, return 401 Unauthorized
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body("Authentication token is missing or invalid.");
        }

        Claims claims;
        try {
            // Parse the JWT token to extract claims
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            // Return 401 if token parsing fails
            return ResponseEntity.status(401).body("Invalid authentication token.");
        }

        String userId = claims.get("id").toString();

        try {
            List<OrderSchema> buyOrders = orderService.getOrderByBuyerId(userId);
            List<OrderSchema> sellOrders = orderService.getOrderBySellerId(userId);

            List<OrderSchema> allOrders = new ArrayList<>();
            allOrders.addAll(buyOrders);
            allOrders.addAll(sellOrders);
            return ResponseEntity.ok(allOrders);
        }
        catch (Exception e) {
            // Return 500 Internal Server Error if there is an issue fetching the offers
            return ResponseEntity.status(500).body("An error occurred while retrieving offers.");
        }
    }   

}
