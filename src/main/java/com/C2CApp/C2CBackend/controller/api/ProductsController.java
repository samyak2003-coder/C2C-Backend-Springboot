package com.C2CApp.C2CBackend.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.C2CApp.C2CBackend.dto.base.ApiResponse;
import com.C2CApp.C2CBackend.dto.product.request.CreateProductRequest;
import com.C2CApp.C2CBackend.dto.product.response.ProductResponse;
import com.C2CApp.C2CBackend.dto.product.response.ProductSummaryResponse;
import com.C2CApp.C2CBackend.exceptions.BusinessException;
import com.C2CApp.C2CBackend.exceptions.JwtAuthenticationException;
import com.C2CApp.C2CBackend.mapper.ProductMapper;
import com.C2CApp.C2CBackend.middleware.JwtAuthenticationMiddleware;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.services.ProductService;
import com.C2CApp.C2CBackend.services.UserService;
import com.C2CApp.C2CBackend.services.OfferService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductsController {
    private final ProductService productService;
    private final JwtAuthenticationMiddleware jwtAuth;
    private final OfferService offerService;

    @Autowired
    public ProductsController(
            ProductService productService,
            UserService userService,
            JwtAuthenticationMiddleware jwtAuth,
            OfferService offerService) {
        this.productService = productService;
        this.jwtAuth = jwtAuth;
        this.offerService = offerService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String sellerId = jwtAuth.validateToken(authHeader).get("id").toString();

            // Create and save the product
            ProductSchema product = ProductMapper.toEntity(request, sellerId);
            ProductSchema savedProduct = productService.createProduct(product);

            return ResponseEntity.ok(ApiResponse.success(ProductMapper.toResponse(savedProduct)));
        } catch (JwtAuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid or expired token"));
        } catch (BusinessException e) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to create product"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductSummaryResponse>>> getProducts(
            @RequestHeader("Authorization") String authHeader) {
        try {
            List<ProductSummaryResponse> productResponses = productService.getProductsWithOffers();
            return ResponseEntity.ok(ApiResponse.success(productResponses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch products"));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductSummaryResponse>> getProductById(
            @PathVariable String productId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            ProductSummaryResponse product = productService.getProductWithOffers(productId);
            return ResponseEntity.ok(ApiResponse.success(product));
        } catch (BusinessException e) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Failed to fetch product"));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable String productId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = jwtAuth.validateToken(authHeader).get("id").toString();
            String userRole = jwtAuth.validateToken(authHeader).get("role").toString();
            
            Optional<ProductSchema> product = productService.getProductByID(productId);
            if (product.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(ApiResponse.error("Product not found"));
            }

            // Verify the user is admin or product owner
            if (!product.get().getSellerId().equals(userId) && 
                !"ADMIN".equals(userRole)) {
                return ResponseEntity.status(403)
                        .body(ApiResponse.error("Not authorized to delete this product"));
            }

            productService.deleteProduct(productId);
            return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
        } catch (BusinessException e) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Internal server error occurred"));
        }
    }
}
