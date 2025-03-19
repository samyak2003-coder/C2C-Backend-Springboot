package com.C2CApp.C2CBackend.repositories;

import com.C2CApp.C2CBackend.entities.ProductSchema;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductSchema, String> {
    Optional<ProductSchema> findByProductId(String productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductSchema p WHERE p.productId = :productId")
    void deleteByProductId(@Param("productId") String productId);
}