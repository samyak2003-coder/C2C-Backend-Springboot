package com.C2CApp.C2CBackend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.C2CApp.C2CBackend.entities.*;
import com.C2CApp.C2CBackend.repositories.OfferRepository;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.UserService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import java.util.List;

@RestController
@RequestMapping("/api/offer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Offer {
    
    private final Dotenv dotenv;
    private final UserService userService;
    private final OfferService offerService;
    private final OfferRepository offerRepository;

    @Autowired
    public Offer(OfferService offerService, UserService userService, OfferRepository offerRepository){
        this.offerService = offerService;
        this.userService = userService;
        this.offerRepository = offerRepository;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<CreateOfferResponse> makeOffer(@RequestHeader("Authorization") String token,@RequestBody CreateOfferInput createOfferInput) {

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

        final String buyerId = claims.get("id").toString();

        Optional<UserSchema> user = userService.getUserById(buyerId);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String productId = createOfferInput.getProductId();
        String sellerId = createOfferInput.getSellerId();
        double offeredPrice = createOfferInput.getOfferedPrice();
        Date offerDate = createOfferInput.getOfferDate();
        String message = createOfferInput.getMessage();
        String status = createOfferInput.getStatus();

        OfferSchema newOffer = new OfferSchema(productId,buyerId,offeredPrice,offerDate,status,message,sellerId);
        offerService.createOffer(newOffer);
        
        return ResponseEntity.ok(new CreateOfferResponse(productId,buyerId,offeredPrice,offerDate,status,message));
    }

    @RequestMapping(
        value = "/getByUserId",
        method = RequestMethod.GET,
        produces = "application/json"
    ) public ResponseEntity<List<OfferSchema>> getOffers(@RequestHeader("Authorization") String token) {
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

        return ResponseEntity.ok(offerService.getOfferByBuyerId(userId));
    }
}
