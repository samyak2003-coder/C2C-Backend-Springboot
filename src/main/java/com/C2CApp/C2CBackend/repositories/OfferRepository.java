package com.C2CApp.C2CBackend.repositories;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.C2CApp.C2CBackend.schema.OfferSchema;

import java.util.Optional;
import java.util.List;


@Repository
public interface OfferRepository extends JpaRepository<OfferSchema, String> {
    @Query("SELECT o FROM OfferSchema o WHERE o.buyerId = :buyerId")
    List<OfferSchema> findByBuyerIdCustom(@Param("buyerId") String buyerId);

    @Query("SELECT o FROM OfferSchema o WHERE o.sellerId = :sellerId")
    List<OfferSchema> findBySellerIdCustom(@Param("sellerId") String sellerId);

    @Query("SELECT o FROM OfferSchema o WHERE o.offerId = :offerId")
    Optional<OfferSchema> findByOfferId(@Param("offerId") String offerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OfferSchema o WHERE o.offerId = :offerId")
    void deleteByOfferId(@Param("offerId") String offerId);

    @Modifying
    @Transactional
    @Query("UPDATE OfferSchema o SET o.status = :status WHERE o.offerId = :offerId")
    void updateOfferStatus(@Param("offerId") String offerId, @Param("status") String status);

    @Query("UPDATE OfferSchema o SET o.status = 'Rejected' WHERE o.productId = :productId AND o.status = 'Pending'")
    @Modifying
    @Transactional
    void rejectAllPendingOffers(@Param("productId") String productId);
}
