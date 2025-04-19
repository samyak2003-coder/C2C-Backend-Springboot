package com.C2CApp.C2CBackend.services;

import org.springframework.stereotype.Service;

import com.C2CApp.C2CBackend.dto.product.response.ProductSummaryResponse;
import com.C2CApp.C2CBackend.mapper.ProductMapper;
import com.C2CApp.C2CBackend.exceptions.BusinessException;
import com.C2CApp.C2CBackend.repositories.ProductRepository;
import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.services.interfaces.IProductService;
import com.C2CApp.C2CBackend.services.interfaces.IProductOfferFacade;
import com.C2CApp.C2CBackend.enums.ProductStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final IProductOfferFacade productOfferFacade;

    @Autowired
    public ProductService(ProductRepository productRepository, @Lazy IProductOfferFacade productOfferFacade) {
        this.productRepository = productRepository;
        this.productOfferFacade = productOfferFacade;
    }

    public ProductSchema updateProductStatus(String id, ProductStatus status) {
        ProductSchema product = productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Product not found"));
        product.setStatus(status);
        return productRepository.save(product);
    }

    @Override
    public List<ProductSchema> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<ProductSchema> getProductByID(String id) {
        return productRepository.findById(id);
    }

    @Override
    public ProductSchema createProduct(ProductSchema product) {
        if (product.getPrice() <= 0) {
            throw new BusinessException("Product price must be greater than zero");
        }
        if (product.getTitle() == null || product.getTitle().trim().isEmpty()) {
            throw new BusinessException("Product title is required");
        }
        return productRepository.save(product);
    }

    @Override
    public ProductSchema updateProduct(String id, ProductSchema product) {
        ProductSchema existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Product not found"));

        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setProductCondition(product.getProductCondition());
        existingProduct.setStatus(product.getStatus());
        
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public ProductSummaryResponse getProductWithOffers(String id) {
        ProductSchema product = productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Product not found"));
        
        // First map basic product details
        ProductSummaryResponse response = ProductMapper.toSummaryResponse(product);
        
        // Then enrich with offers data using the facade
        response.setOffers(productOfferFacade.getOffersForProduct(product.getId()));
        
        return response;
    }

    public List<ProductSummaryResponse> getProductsWithOffers() {
        return getProducts().stream()
            .map(product -> {
                ProductSummaryResponse response = ProductMapper.toSummaryResponse(product);
                response.setOffers(productOfferFacade.getOffersForProduct(product.getId()));
                return response;
            })
            .collect(java.util.stream.Collectors.toList());
    }
}
