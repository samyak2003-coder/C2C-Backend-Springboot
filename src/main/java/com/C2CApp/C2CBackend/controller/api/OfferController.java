package com.C2CApp.C2CBackend.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.C2CApp.C2CBackend.enums.OfferStatus;
import com.C2CApp.C2CBackend.services.ProductOfferFacade;

import com.C2CApp.C2CBackend.dto.base.ApiResponse;
import com.C2CApp.C2CBackend.exceptions.BusinessException;
import com.C2CApp.C2CBackend.dto.offer.create.CreateOfferRequest;
import com.C2CApp.C2CBackend.dto.offer.create.CreateOfferResponse;
import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;
import com.C2CApp.C2CBackend.dto.offer.update.UpdateOfferRequest;
import com.C2CApp.C2CBackend.dto.offer.update.UpdateOfferResponse;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.ProductService;
import com.C2CApp.C2CBackend.services.UserService;
import com.C2CApp.C2CBackend.middleware.JwtAuthenticationMiddleware;
import com.C2CApp.C2CBackend.mapper.OfferMapper;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    
    private final OfferService offerService;
    private final ProductService productService;
    private final JwtAuthenticationMiddleware jwtAuth;
    private final ProductOfferFacade productOfferFacade;

    @Autowired
    public OfferController(
            UserService userService,
            OfferService offerService,
            ProductService productService,
            ProductOfferFacade productOfferFacade,
            JwtAuthenticationMiddleware jwtAuth) {
        this.offerService = offerService;
        this.productService = productService;
        this.productOfferFacade = productOfferFacade;
        this.jwtAuth = jwtAuth;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateOfferResponse>> createOffer(
            @Valid @RequestBody CreateOfferRequest request) {
        try {
            // Validate product exists and is available
            Optional<ProductSchema> product = productService.getProductByID(request.getProductId());
            if (product.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Product not found"));
            }

            // Create and save the offer
            OfferSchema offer = OfferMapper.toEntity(request);
            OfferSchema createdOffer = offerService.createOffer(offer);
            return ResponseEntity.ok(
                ApiResponse.success(OfferMapper.toCreateResponse(createdOffer))
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<ApiResponse<GetOfferResponse>> getOffer(
            @PathVariable String offerId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            jwtAuth.validateToken(authHeader); // Validate token
            
            Optional<OfferSchema> offer = offerService.getOfferById(offerId);
            if (offer.isEmpty()) {
                return ResponseEntity.notFound()
                    .build();
            }

            Optional<ProductSchema> product = productService.getProductByID(offer.get().getProductId());
            
            return ResponseEntity.ok(
                ApiResponse.success(OfferMapper.toGetResponse(offer.get(), product.orElse(null)))
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/my-offers")
    public ResponseEntity<ApiResponse<List<GetOfferResponse>>> getMyOffers(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = jwtAuth.validateToken(authHeader).get("id").toString();
            
            List<GetOfferResponse> offers = offerService.getOfferByBuyerId(userId);

            return ResponseEntity.ok(ApiResponse.success(offers));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/received")
    public ResponseEntity<ApiResponse<List<GetOfferResponse>>> getReceivedOffers(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = jwtAuth.validateToken(authHeader).get("id").toString();
            List<GetOfferResponse> offers = offerService.getOfferBySellerId(userId);
            return ResponseEntity.ok(ApiResponse.success(offers));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<ApiResponse<UpdateOfferResponse>> updateOffer(
            @PathVariable String offerId,
            @Valid @RequestBody UpdateOfferRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            jwtAuth.validateToken(authHeader); // Validate token
            
            Optional<OfferSchema> offer = offerService.getOfferById(offerId);
            if (offer.isEmpty()) {
                return ResponseEntity.notFound()
                    .build();
            }

            // Get current offer to retrieve productId
            Optional<OfferSchema> currentOffer = offerService.getOfferById(offerId);
            if (currentOffer.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Handle the offer status update through the facade
            if (request.getStatus() == OfferStatus.ACCEPTED) {
                productOfferFacade.handleOfferAcceptance(offerId, currentOffer.get().getProductId());
            } else {
                offerService.updateOfferStatus(offerId, request.getStatus());
            }

            // Get the updated offer for response
            OfferSchema updatedOffer = offerService.getOfferById(offerId).get();
            
            return ResponseEntity.ok(
                ApiResponse.success(OfferMapper.toUpdateResponse(updatedOffer))
            );
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}
