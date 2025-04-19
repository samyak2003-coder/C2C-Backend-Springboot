package com.C2CApp.C2CBackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import com.C2CApp.C2CBackend.schema.ProductSchema;
import com.C2CApp.C2CBackend.enums.ProductStatus;



/**
 * Repository interface for Product operations.
 * Built-in JPA methods available:
 * - save(ProductSchema entity)
 * - findAll()
 * - findById(String id)
 * - deleteById(String id)
 * - existsById(String id)
 */

@Repository
public interface ProductRepository extends JpaRepository<ProductSchema, String> {
    // Custom method for updating product status
    @Modifying
    @Transactional
    @Query("UPDATE ProductSchema p SET p.status = :status WHERE p.id = :id")
    void updateStatus(@Param("id") String id, @Param("status") ProductStatus status);
}
