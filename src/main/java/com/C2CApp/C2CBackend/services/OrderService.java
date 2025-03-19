package com.C2CApp.C2CBackend.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.C2CApp.C2CBackend.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import com.C2CApp.C2CBackend.entities.OrderSchema;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public List<OrderSchema> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderSchema> getOrderByBuyerId(String buyerId){
        return orderRepository.findByBuyerId(buyerId);
    }

    public List<OrderSchema> getOrderBySellerId(String sellerId){
        return orderRepository.findBySellerId(sellerId);
    }

    public void createOrder(OrderSchema order){
        orderRepository.save(order);
    }

    public void deleteOrder(String orderId){
        orderRepository.deleteByOrderId(orderId);
    }
}
