package com.C2CApp.C2CBackend.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.C2CApp.C2CBackend.dto.auth.request.SignInRequest;
import com.C2CApp.C2CBackend.dto.auth.request.SignUpRequest;
import com.C2CApp.C2CBackend.dto.product.request.CreateProductRequest;
import com.C2CApp.C2CBackend.dto.offer.create.CreateOfferRequest;
import com.C2CApp.C2CBackend.dto.offer.update.UpdateOfferRequest;
import com.C2CApp.C2CBackend.dto.offer.request.RemoveOfferDetails;
import com.C2CApp.C2CBackend.dto.admin.request.DeleteAdminRequest;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.services.OfferService;
import com.C2CApp.C2CBackend.services.ProductService;
import com.C2CApp.C2CBackend.enums.OfferStatus;

@Controller
public class ViewController {

    private final ProductService productService;
    private final OfferService offerService;

    @Autowired
    public ViewController(
            ProductService productService, 
            OfferService offerService) {
        this.productService = productService;
        this.offerService = offerService;
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signUpDetails", new SignUpRequest());
        return "signup";
    }

    @GetMapping("/signin")
    public String login(Model model) {
        model.addAttribute("signInDetails", new SignInRequest());
        return "signin";
    }

    @GetMapping("/sellProducts")
    public String products(Model model) {
        model.addAttribute("sellProductDetails", new CreateProductRequest());
        return "sellProduct";
    }
    
    @GetMapping("/productView")
    public String showProductView(@RequestParam("productId") String productId, Model model) {
        System.out.println("Received product ID in ViewController: " + productId);
        model.addAttribute("productId", productId); // Pass productId to the view
        model.addAttribute("createOfferDetails", new CreateOfferRequest());
        return "productView";
    }

    @GetMapping("/myOffers")
    public String showMyOffers(Model model) {
        return "myOffers";
    }

    @GetMapping("/receivedOffers")
    public String showReceivedOffers(Model model) {
        return "receivedOffers";
    }
       
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("deleteEntityDetails", new DeleteAdminRequest());
        return "admin";
    }

    @GetMapping("/admin-login")
    public String adminLogin(Model model) {
        model.addAttribute("signInDetails", new SignInRequest());
        return "admin-login";
    }
    
    @PostMapping("/update-offer")
    public String updateOffer(
            @RequestParam("offerId") String offerId,
            @RequestParam("status") String status) {
        try {
            offerService.updateOfferStatus(offerId, OfferStatus.valueOf(status.toUpperCase()));
        } catch (Exception ex) {
            System.err.println("Error updating offer: " + ex.getMessage());
        }
        return "redirect:/receivedOffers";
    }

    @PostMapping("/remove-offer")
    public String removeOffer(@RequestParam("offerId") String offerId) {
        try {
            offerService.updateOfferStatus(offerId, OfferStatus.REJECTED);
        } catch (Exception ex) {
            System.err.println("Error removing offer: " + ex.getMessage());
        }
        return "redirect:/receivedOffers";
    }
}
