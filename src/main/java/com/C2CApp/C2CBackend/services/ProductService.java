package com.C2CApp.C2CBackend.services;

import org.springframework.stereotype.Service;

import com.C2CApp.C2CBackend.entities.ProductSchema;
import com.C2CApp.C2CBackend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductSchema> getProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductSchema> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    public void createProduct(ProductSchema product){
        productRepository.save(product);
    }

    public void updateProduct(String productId, ProductSchema product){
        Optional<ProductSchema> productOptional = productRepository.findById(productId);
        if(productOptional.isPresent()){
            ProductSchema updatedProduct = productOptional.get();
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setCategory(product.getCategory());
            updatedProduct.setProductCondition(product.getProductCondition());
            updatedProduct.setStatus(product.getStatus());
        }
    }

    public void deleteProduct(String productId){
        productRepository.deleteById(productId);
    }
}
