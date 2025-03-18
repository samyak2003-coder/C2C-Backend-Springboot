package com.C2CApp.C2CBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.C2CApp.C2CBackend.entities.*;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.ProductService;

import java.util.Optional;


@Controller
public class MainController {


    private final ProductService productService;
    private final OfferService offerService;

    @Autowired
    public MainController(ProductService productService,OfferService offerService) {
        this.productService = productService;
        this.offerService = offerService;
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
    public String showProductView(@RequestParam("productId") String productId, Model model) {
        ProductSchema product = productService.getByProductId(productId).orElse(null);
    
        if (product != null) {
            model.addAttribute("product", product);
        }
        model.addAttribute("createOfferDetails", new CreateOfferInput());
        return "productView";
    }

    @GetMapping("/offers")
    public String showOffers(Model model){
        model.addAttribute("deleteOfferDetails", new DeleteOfferDetails());
        model.addAttribute("createOrderDetails", new CreateOrderDto());
        model.addAttribute("updateOfferDetails", new UpdateOfferInput());
        return "offers";
    }

    @GetMapping("/orders")
    public String orders(){
        return "orders";
    }
       
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
