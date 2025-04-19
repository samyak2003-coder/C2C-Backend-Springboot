package com.C2CApp.C2CBackend.services.interfaces;

import java.util.List;
import java.util.Optional;
import com.C2CApp.C2CBackend.schema.ProductSchema;

public interface IProductService {
    List<ProductSchema> getProducts();
    Optional<ProductSchema> getProductByID(String id);
    ProductSchema createProduct(ProductSchema product);
    ProductSchema updateProduct(String id, ProductSchema product);
    void deleteProduct(String id);
}