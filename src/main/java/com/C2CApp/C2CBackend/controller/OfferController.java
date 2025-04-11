package com.C2CApp.C2CBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.C2CApp.C2CBackend.dto.OfferWithProductDto;
import com.C2CApp.C2CBackend.entities.CreateOfferInput;
import com.C2CApp.C2CBackend.entities.DeleteAdminDetails;
import com.C2CApp.C2CBackend.entities.RemoveOfferDetails;
import com.C2CApp.C2CBackend.entities.UpdateOfferInput;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.schema.UserSchema;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.ProductService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OfferController {
    private final OfferService offerService;
    private final UserService userService;
    private final ProductService productService;
    private final Dotenv dotenv;

    @Autowired
    public OfferController(UserService userService, Dotenv dotenv, OfferService offerService,
            ProductService productService) {
        this.offerService = offerService;
        this.userService = userService;
        this.productService = productService;
        this.dotenv = dotenv;
    }

    @PostMapping("/create-offer")
    public String create(
            @Valid @ModelAttribute("createOfferDetails") CreateOfferInput form,
            BindingResult bindingResult,
            HttpSession session,
            HttpServletResponse response) {
        // Check for binding errors
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            session.setAttribute("offerStatus", "BINDING_ERROR");
            return "redirect:/productView?productId=" + form.getProductId();
        }

        String token = form.getToken();
        Double offeredPrice = form.getOfferedPrice();
        String productId = form.getProductId();
        String sellerId = form.getSellerId();
        Date offerDate = form.getOfferDate();
        String status = "Pending"; // Always start with Pending status

        // Check if product is already sold
        try {
            Optional<ProductSchema> product = productService.getByProductId(productId);
            if (product.isPresent() && "Sold".equals(product.get().getStatus())) {
                session.setAttribute("offerStatus", "PRODUCT_SOLD");
                return "redirect:/productView?productId=" + productId;
            }
        } catch (Exception e) {
            session.setAttribute("offerStatus", "PRODUCT_NOT_FOUND");
            return "redirect:/productView?productId=" + productId;
        }

        // Validate token and get buyer ID
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            session.setAttribute("offerStatus", "TOKEN_PARSE_ERROR");
            return "redirect:/productView?productId=" + productId;
        }

        final String buyerId = claims.get("id").toString();

        // Validate user exists
        Optional<UserSchema> user = userService.getUserById(buyerId);
        if (user.isEmpty()) {
            session.setAttribute("offerStatus", "USER_NOT_FOUND");
            return "redirect:/productView?productId=" + productId;
        }

        // Validate price
        if (offeredPrice == null || offeredPrice <= 0) {
            session.setAttribute("offerStatus", "INVALID_PRICE");
            return "redirect:/productView?productId=" + productId;
        }

        // Create and attempt to save the offer
        OfferSchema newOffer = new OfferSchema(buyerId, offerDate, offeredPrice, productId, status, sellerId);
        boolean offerCreated = offerService.createOffer(newOffer);

        if (!offerCreated) {
            session.setAttribute("offerStatus", "OFFER_FAILED");
            return "redirect:/productView?productId=" + productId;
        }

        session.setAttribute("offerStatus", "OFFER_SUCCESS");
        session.setAttribute("status", "Pending"); // Set initial status
        return "redirect:/productView?productId=" + productId;

    }

    // Page mappings
    @GetMapping("/my-offers")
    public String showMyOffers() {
        return "myOffers";
    }

    @GetMapping("/received-offers")
    public String showReceivedOffers() {
        return "receivedOffers";
    }

    // API endpoints
    @GetMapping("/api/my-offers")
    public ResponseEntity<?> getMyOffers(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        // Check if Authorization header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Auth Error: Authorization header is missing or malformed");
            return ResponseEntity.status(401).body("Authentication token is missing or invalid.");
        }

        Claims claims;
        try {
            System.out
                    .println("Processing token: " + authHeader.substring(0, Math.min(authHeader.length(), 20)) + "...");
            // Parse the JWT token to extract claims
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            // Return 401 if token parsing fails
            System.out.println("Auth Error: Token validation failed - " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid authentication token.");
        }

        String userId = claims.get("id").toString();

        try {
            List<OfferWithProductDto> buyerOffers = offerService.getOfferByBuyerId(userId);
            HashMap<String, Object> response = new HashMap<>();
            response.put("offers", buyerOffers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Return 500 Internal Server Error if there is an issue fetching the offers
            System.out.println("Error retrieving offers: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while retrieving offers.");
        }
    }

    @GetMapping("/api/received-offers")
    public ResponseEntity<?> getReceivedOffers(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Auth Error: Authorization header is missing or malformed");
            return ResponseEntity.status(401).body("Authentication token is missing or invalid.");
        }

        Claims claims;
        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            System.out.println("Auth Error: Token validation failed - " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid authentication token.");
        }

        String userId = claims.get("id").toString();

        try {
            List<OfferWithProductDto> sellerOffers = offerService.getOfferBySellerId(userId);
            HashMap<String, Object> response = new HashMap<>();
            response.put("offers", sellerOffers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error retrieving offers: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while retrieving offers.");
        }
    }

    @PostMapping("update-offer")
    public String updateOffer(
            @Valid @ModelAttribute("updateOfferDetails") UpdateOfferInput form,
            BindingResult bindingResult,
            HttpSession session,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            session.setAttribute("offerUpdateStatus", "UPDATE_FAILED");
            return "redirect:/received-offers";
        }

        String offerId = form.getOfferId();
        String status = form.getStatus();

        try {
            offerService.updateOfferStatus(offerId, status);
            session.setAttribute("offerUpdateStatus", "UPDATE_SUCCESS");
        } catch (Exception e) {
            session.setAttribute("offerUpdateStatus", "UPDATE_FAILED");
        }

        return "redirect:/received-offers";
    }

    @PostMapping("remove-offer")
    public String rejectOffer(
            @ModelAttribute("removeOfferDetails") RemoveOfferDetails form,
            HttpSession session) {
        try {
            // Instead of deleting, update status to Rejected
            offerService.updateOfferStatus(form.getOfferId(), "Rejected");
            session.setAttribute("offerUpdateStatus", "REJECT_SUCCESS");
        } catch (Exception e) {
            session.setAttribute("offerUpdateStatus", "REJECT_FAILED");
        }
        return "redirect:/received-offers";
    }
}
