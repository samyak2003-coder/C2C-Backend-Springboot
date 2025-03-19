package com.C2CApp.C2CBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.C2CApp.C2CBackend.entities.CreateOfferInput;
import com.C2CApp.C2CBackend.entities.DeleteAdminDetails;
import com.C2CApp.C2CBackend.entities.OfferSchema;
import com.C2CApp.C2CBackend.entities.RemoveOfferDetails;
import com.C2CApp.C2CBackend.entities.UpdateOfferInput;
import com.C2CApp.C2CBackend.entities.UserSchema;
import com.C2CApp.C2CBackend.services.OfferService;
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

import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.HashMap;

@Controller
public class OfferController {
    private final OfferService offerService;
    private final UserService userService;
    private final Dotenv dotenv;

    @Autowired
    public OfferController(UserService userService,Dotenv dotenv,OfferService offerService){
        this.offerService = offerService;
        this.userService = userService;
        this.dotenv = dotenv;
    }

@PostMapping("/create-offer")
public String create(
    @Valid @ModelAttribute("createOfferDetails") CreateOfferInput form,
    BindingResult bindingResult,
    HttpSession session,
    HttpServletResponse response
) {
    // Proceed with your normal processing
    String token = form.getToken();
    Double offeredPrice = form.getOfferedPrice();
    String productId = form.getProductId();
    String sellerId = form.getSellerId();
    Date offerDate = form.getOfferDate();
    String status = form.getStatus();

        // Check if there are any binding errors and log them
        if (bindingResult.hasErrors()) {
            System.out.println("Binding Errors:");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            session.setAttribute("OfferStatus", "BINDING_ERROR");
            return "productView";
        }

    Claims claims;

    try {
        claims = Jwts.parser()
                .setSigningKey(dotenv.get("JWT_SECRET"))
                .parseClaimsJws(token)
                .getBody();
    } catch (JwtException e) {
        session.setAttribute("OfferStatus", "TOKEN_PARSE_ERROR");
        return "productView";
    }

    final String buyerId = claims.get("id").toString();

    Optional<UserSchema> user = userService.getUserById(buyerId);

    if (user.isEmpty()) {
        session.setAttribute("OfferStatus", "USER_NOT_FOUND");
        return "productView";

    }

    if (offeredPrice.isNaN()) {
        session.setAttribute("OfferStatus", "PRICE_EMPTY");
        return "productView";

    }

    OfferSchema newOffer = new OfferSchema(buyerId,offerDate, offeredPrice, productId, status,sellerId);
    offerService.createOffer(newOffer);

    return "home";

}

@GetMapping("/get-offers")
public ResponseEntity<?> getByUserId(HttpServletRequest request) {
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
    List<OfferSchema> buyerOffers = offerService.getOfferByBuyerId(userId);
    List<OfferSchema> sellerOffers = offerService.getOfferBySellerId(userId);

    // Create a HashMap to categorize the offers as buy or sell
    HashMap<String, List<OfferSchema>> offerMap = new HashMap<>();

    // Add the offers to the HashMap
    offerMap.put("buy", buyerOffers);
    offerMap.put("sell", sellerOffers);

    // Return the offers map
    return ResponseEntity.ok(offerMap);

    } catch (Exception e) {
        // Return 500 Internal Server Error if there is an issue fetching the offers
        return ResponseEntity.status(500).body("An error occurred while retrieving offers.");
    }
}

    @PostMapping("/delete-offer")
    public String deleteOffer(@Valid @ModelAttribute("deleteEntityDetails") DeleteAdminDetails form,
        BindingResult bindingResult,
        HttpSession session,
        HttpServletResponse response) {
            String offerId = form.getEntityId();
            offerService.deleteOfferById(offerId);
            return "offers";
    }

    @PostMapping("/remove-offer")
    public String deleteOffer(@Valid @ModelAttribute("removeOfferDetails") RemoveOfferDetails form,
        BindingResult bindingResult,
        HttpSession session,
        HttpServletResponse response) {
            String offerId = form.getOfferId();
            offerService.deleteOfferById(offerId);
            return "offers";
    }

    @PostMapping("update-offer")
    public String updateOffer(@Valid @ModelAttribute("updateOfferDetails") UpdateOfferInput form,
    BindingResult bindingResult,
        HttpSession session,
        HttpServletResponse response){
            String offerId = form.getOfferId();
            String status = form.getStatus();

            offerService.updateOfferStatus(offerId, status);            
            return "offers";
    }
}
