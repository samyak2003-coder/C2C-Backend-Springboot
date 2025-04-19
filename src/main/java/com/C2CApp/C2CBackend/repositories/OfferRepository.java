package com.C2CApp.C2CBackend.repositories;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.C2CApp.C2CBackend.schema.OfferSchema;
import com.C2CApp.C2CBackend.enums.OfferStatus;

import java.util.List;

/**
 * Repository interface for Offer operations.
 * Built-in JPA methods available:
 * - save(OfferSchema entity)
 * - findAll()
 * - findById(String id)
 * - deleteById(String id)
 * - existsById(String id)
 * These methods are automatically implemented by Spring Data JPA
 */
@Repository
public interface OfferRepository extends JpaRepository<OfferSchema, String> {
    // Custom methods for relationship lookups
    @Query("SELECT o FROM OfferSchema o WHERE o.buyerId = :buyerId")
    List<OfferSchema> findByBuyerId(@Param("buyerId") String buyerId);

    @Query("SELECT o FROM OfferSchema o WHERE o.sellerId = :sellerId")
    List<OfferSchema> findBySellerId(@Param("sellerId") String sellerId);

    @Query("SELECT o FROM OfferSchema o WHERE o.productId = :productId")
    List<OfferSchema> findByProductId(@Param("productId") String productId);

    // Custom methods for status management
    @Modifying
    @Transactional
    @Query("UPDATE OfferSchema o SET o.status = :status WHERE o.id = :id")
    void updateStatus(@Param("id") String id, @Param("status") OfferStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE OfferSchema o SET o.status = :rejectedStatus WHERE o.productId = :productId AND o.status = :pendingStatus AND o.id != :excludeOfferId")
    void rejectAllPendingOffers(
        @Param("productId") String productId, 
        @Param("rejectedStatus") OfferStatus rejectedStatus,
        @Param("pendingStatus") OfferStatus pendingStatus,
        @Param("excludeOfferId") String excludeOfferId
    );
}
