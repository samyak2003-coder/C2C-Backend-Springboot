package com.C2CApp.C2CBackend.controller.api;

import com.C2CApp.C2CBackend.dto.base.ApiResponse;
import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;
import com.C2CApp.C2CBackend.dto.user.response.UserResponse;
import com.C2CApp.C2CBackend.exceptions.BusinessException;
import com.C2CApp.C2CBackend.mapper.OfferMapper;
import com.C2CApp.C2CBackend.mapper.UserMapper;
import com.C2CApp.C2CBackend.middleware.JwtAuthenticationMiddleware;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.dto.product.response.ProductResponse;
import com.C2CApp.C2CBackend.schema.UserSchema;
import com.C2CApp.C2CBackend.mapper.ProductMapper;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.ProductService;
import com.C2CApp.C2CBackend.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final OfferService offerService;
    private final ProductService productService;
    private final JwtAuthenticationMiddleware jwtAuth;

    @Autowired
    public AdminController(
            UserService userService, 
            OfferService offerService, 
            ProductService productService, 
            JwtAuthenticationMiddleware jwtAuth) {
        this.userService = userService;
        this.offerService = offerService;
        this.productService = productService;
        this.jwtAuth = jwtAuth;
    }

    private void checkAdminAccess(String authHeader) {
        String userId = jwtAuth.validateToken(authHeader).get("id").toString();
        if (!userService.isAdmin(userId)) {
            throw new BusinessException("Admin access required");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers(
            @RequestHeader("Authorization") String authHeader) {
        try {
            checkAdminAccess(authHeader);
            List<UserSchema> users = userService.getAllUsers();
            List<UserResponse> userResponses = users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(userResponses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/offers")
    public ResponseEntity<ApiResponse<List<GetOfferResponse>>> getOffers(
            @RequestHeader("Authorization") String authHeader) {
        try {
            checkAdminAccess(authHeader);
            List<OfferSchema> offers = offerService.getAllOffers();
            List<GetOfferResponse> offerResponses = offers.stream()
                .map(offer -> {
                    ProductSchema product = productService.getProductByID(offer.getProductId()).orElse(null);
                    return OfferMapper.toGetResponse(offer, product);
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(offerResponses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(
            @RequestHeader("Authorization") String authHeader) {
        try {
            checkAdminAccess(authHeader);
            List<ProductSchema> products = productService.getProducts();
            List<ProductResponse> productResponses = products.stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success(productResponses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable String userId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            checkAdminAccess(authHeader);
            userService.deleteUser(userId);
            return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable String productId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            checkAdminAccess(authHeader);
            productService.deleteProduct(productId);
            return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/offers/{offerId}")
    public ResponseEntity<ApiResponse<Void>> deleteOffer(
            @PathVariable String offerId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            checkAdminAccess(authHeader);
            offerService.deleteOfferById(offerId);
            return ResponseEntity.ok(ApiResponse.success(null, "Offer deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}
