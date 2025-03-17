package com.C2CApp.C2CBackend.controller;

import java.util.Optional;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.C2CApp.C2CBackend.entities.ProductSchema;
import com.C2CApp.C2CBackend.entities.SellProductInput;
import com.C2CApp.C2CBackend.entities.SignInDetails;
import com.C2CApp.C2CBackend.entities.UserSchema;
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

import java.util.List;

@Controller
public class ProductsController {
    private final ProductService productService;
    private final UserService userService;
    private final Dotenv dotenv;

    @Autowired
    public ProductsController(UserService userService,Dotenv dotenv,ProductService productService){
        this.productService = productService;
        this.userService = userService;
        this.dotenv = dotenv;
    }
    
    @PostMapping("/create-products")
    public String create(
        @Valid @ModelAttribute("sellProductDetails") SellProductInput form,
        BindingResult bindingResult,
        HttpSession session,
        HttpServletResponse response
    ){
        if (bindingResult.hasErrors()) {
            session.setAttribute("ProductStatus", "FAILED");
            return "products";
        }

        String title = form.getTitle();
        String description = form.getDescription();
        Double price = form.getPrice();
        String category = form.getCategory();
        String productCondition = form.getProductCondition();
        String status = form.getStatus();
        String token = form.getToken();

        Claims claims;

        try {
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            session.setAttribute("ProductStatus","FAILED");
            return "products";
        }

        final String sellerId = claims.get("id").toString();

        Optional<UserSchema> user = userService.getUserById(sellerId);

        if (user.isEmpty()) {
            session.setAttribute("ProductStatus","FAILED");
            return "products";
        }

        if(title.isEmpty() || price.isNaN()){
            session.setAttribute("ProductStatus","FAILED");
            return "products";
        }

        ProductSchema newProduct=  new ProductSchema(title,description,price,category,productCondition,sellerId,status);
        productService.createProduct(newProduct);

        return "home";
    }

    @GetMapping("/get-products")
    public ResponseEntity<List<ProductSchema>> getProducts(HttpServletRequest request){
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/get-product/{productId}")
    public ResponseEntity<ProductSchema> getById(@PathVariable("productId") String productId) {
        Optional<ProductSchema> product = productService.getByProductId(productId);
        
        if (!product.isPresent()) {
            return ResponseEntity.notFound().build(); 
        }
    
        return ResponseEntity.ok(product.get()); 
    }
    
}
