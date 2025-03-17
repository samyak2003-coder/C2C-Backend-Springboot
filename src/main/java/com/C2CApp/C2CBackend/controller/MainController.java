package com.C2CApp.C2CBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.C2CApp.C2CBackend.entities.*;
import com.C2CApp.C2CBackend.services.ProductService;

@Controller
public class MainController {


    private final ProductService productService;

    @Autowired
    public MainController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("signUpDetails", new SignUpDetails());
        return "signup";
    }

    @GetMapping("/signin")
    public String login(Model model) {
        model.addAttribute("signInDetails", new SignInDetails());
        return "signin";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("sellProductDetails", new SellProductInput());
        return "products";
    }

        @GetMapping("/productView")
    public String viewProductDetails(@RequestParam("productId") String productId, Model model) {
        ProductSchema product = productService.getByProductId(productId).orElse(null);
        if (product != null) {
            model.addAttribute("product", product);
            return "productView"; 
        } else {
            model.addAttribute("error", "Product not found!");
            return "error"; 
        }
    }


    @GetMapping("/")
    public String home(){
        return "home";
    }
}
