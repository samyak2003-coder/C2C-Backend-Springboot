package com.C2CApp.C2CBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.C2CApp.C2CBackend.entities.CreateOfferInput;
import com.C2CApp.C2CBackend.entities.DeleteAdminDetails;
import com.C2CApp.C2CBackend.entities.RemoveOfferDetails;
import com.C2CApp.C2CBackend.entities.SignInDetails;
import com.C2CApp.C2CBackend.entities.SignUpDetails;
import com.C2CApp.C2CBackend.entities.SellProductDetails;
import com.C2CApp.C2CBackend.entities.UpdateOfferInput;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.ProductService;



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

    @GetMapping("/sellProducts")
    public String products(Model model) {
        model.addAttribute("sellProductDetails", new SellProductDetails());
        return "sellProduct";
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
        model.addAttribute("updateOfferDetails", new UpdateOfferInput());
        model.addAttribute("removeOfferDetails", new RemoveOfferDetails());
        return "offers";
    }
       
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("deleteEntityDetails", new DeleteAdminDetails());
        return "admin";
    }

    @GetMapping("/admin-login")
    public String adminLogin(Model model){
        model.addAttribute("signInDetails", new SignInDetails());
        return "admin-login";
    }
}
