package com.C2CApp.C2CBackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.C2CApp.C2CBackend.entities.OrderSchema;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderSchema, String> {
    Optional<OrderSchema> findById(String orderId);
    List<OrderSchema> findByBuyerId(String buyerId);
    List<OrderSchema> findBySellerId(String sellerId);
}
