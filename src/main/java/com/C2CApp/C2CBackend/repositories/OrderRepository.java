package com.C2CApp.C2CBackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.C2CApp.C2CBackend.entities.OrderSchema;

import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderSchema, String> {
    List<OrderSchema> findByBuyerId(String buyerId);
    List<OrderSchema> findBySellerId(String sellerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderSchema o WHERE o.orderId = :orderId")
    void deleteByOrderId(@Param("orderId") String orderId);
}
