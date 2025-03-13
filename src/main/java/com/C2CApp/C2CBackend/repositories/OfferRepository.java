package com.C2CApp.C2CBackend.repositories;

import com.C2CApp.C2CBackend.entities.OfferSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;


@Repository
public interface OfferRepository extends JpaRepository<OfferSchema, String> {
    Optional<OfferSchema> findById(String offerId);
    List<OfferSchema> findByBuyerId(String buyerId);
}
