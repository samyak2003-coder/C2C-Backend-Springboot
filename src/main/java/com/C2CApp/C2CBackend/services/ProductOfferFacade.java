package com.C2CApp.C2CBackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.C2CApp.C2CBackend.dto.offer.get.GetOfferResponse;
import com.C2CApp.C2CBackend.enums.OfferStatus;
import com.C2CApp.C2CBackend.enums.ProductStatus;
import com.C2CApp.C2CBackend.exceptions.BusinessException;
import com.C2CApp.C2CBackend.mapper.OfferMapper;
import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.services.interfaces.IProductOfferFacade;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOfferFacade implements IProductOfferFacade {
    private final ProductService productService;
    private final OfferService offerService;

    @Autowired
    public ProductOfferFacade(@Lazy ProductService productService, @Lazy OfferService offerService) {
        this.productService = productService;
        this.offerService = offerService;
    }

    @Override
    public Optional<ProductSchema> findProductById(String productId) {
        return productService.getProductByID(productId);
    }

    @Override
    public void validateProductAvailabilityForOffer(String productId) {
        Optional<ProductSchema> productOpt = findProductById(productId);
        if (productOpt.isEmpty()) {
            throw new BusinessException("Product not found");
        }
        if (ProductStatus.SOLD.equals(productOpt.get().getStatus())) {
            throw new BusinessException("Product is already sold");
        }
    }

    @Override
    public void markProductAsSold(String productId) {
        // First validate the product exists
        if (findProductById(productId).isEmpty()) {
            throw new BusinessException("Product not found");
        }
        productService.updateProductStatus(productId, ProductStatus.SOLD);
    }

    @Override
    public List<GetOfferResponse> getOffersForProduct(String productId) {
        // First validate the product exists
        if (findProductById(productId).isEmpty()) {
            throw new BusinessException("Product not found");
        }

        List<OfferSchema> offers = offerService.findOffersByProductIdRaw(productId);
        return offers.stream()
            .map(offer -> {
                ProductSchema product = findProductById(offer.getProductId()).orElse(null);
                return OfferMapper.toGetResponse(offer, product);
            })
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void rejectAllPendingOffersForProduct(String productId) {
        // First validate the product exists
        if (findProductById(productId).isEmpty()) {
            throw new BusinessException("Product not found");
        }
        offerService.rejectAllPendingOffersForProduct(productId, null);
    }

    @Override
    public void handleOfferAcceptance(String offerId, String productId) {
        // First validate both offer and product exist
        if (offerService.getOfferById(offerId).isEmpty()) {
            throw new BusinessException("Offer not found");
        }
        if (findProductById(productId).isEmpty()) {
            throw new BusinessException("Product not found");
        }

        // First update the offer status
        offerService.updateOfferStatus(offerId, OfferStatus.ACCEPTED);
        
        // Then mark product as sold
        markProductAsSold(productId);
        
        // Finally reject all other pending offers except the accepted one
        offerService.rejectAllPendingOffersForProduct(productId, offerId);
    }

    @Override
    public void updateProductStatusAndOffers(String productId, ProductStatus status) {
        // First validate the product exists
        if (findProductById(productId).isEmpty()) {
            throw new BusinessException("Product not found");
        }

        productService.updateProductStatus(productId, status);
        if (status == ProductStatus.SOLD) {
            rejectAllPendingOffersForProduct(productId);
        }
    }
}
