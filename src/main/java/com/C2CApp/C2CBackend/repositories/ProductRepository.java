package com.C2CApp.C2CBackend.repositories;

import com.C2CApp.C2CBackend.entities.ProductSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductSchema, String> {
    Optional<ProductSchema> findByProductId(String productId);
}