package com.C2CApp.C2CBackend.controller;

import java.util.Optional;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.C2CApp.C2CBackend.entities.CreateOrderDto;
import com.C2CApp.C2CBackend.entities.UserSchema;
import com.C2CApp.C2CBackend.repositories.OrderRepository;
import com.C2CApp.C2CBackend.services.UserService;
import com.C2CApp.C2CBackend.services.OrderService;
import com.C2CApp.C2CBackend.entities.OrderSchema;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {
    private final Dotenv dotenv;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, OrderRepository orderRepository){
        this.orderService = orderService;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    @RequestMapping(
        value = "/create",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json")
    public ResponseEntity<CreateOrderDto> create(@RequestHeader("Authorization") String token, @RequestBody CreateOrderDto createOrderDto){
               if (token == null) {
            return ResponseEntity.badRequest().build();
        }

        Claims claims;

        try {
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return ResponseEntity.badRequest().build();
        }

        final String buyerId = claims.get("id").toString();

        Optional<UserSchema> user = userService.getUserById(buyerId);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String sellerId = createOrderDto.getSellerId();
        String productId = createOrderDto.getProductId();
        Date orderDate = createOrderDto.getOrderDate();
        String paymentMethod = createOrderDto.getPaymentMethod();

        OrderSchema newOrder = new OrderSchema(buyerId,sellerId,productId,orderDate,paymentMethod);
        orderService.createOrder(newOrder);

        return ResponseEntity.ok(new CreateOrderDto(sellerId,productId,orderDate,paymentMethod));
    }

    @RequestMapping(
    value = "/getBySellerId",
    method = RequestMethod.GET,
    produces = "application/json" )
    public ResponseEntity<List<OrderSchema>> getOrderBySellerId(@RequestParam String userId) {
        return ResponseEntity.ok(orderRepository.findBySellerId(userId));
    }   

    @RequestMapping(
    value = "/getByBuyerId",
    method = RequestMethod.GET,
    produces = "application/json"
)
    public ResponseEntity<List<OrderSchema>> getOrderByBuyerId(@RequestParam String userId) {
        return ResponseEntity.ok(orderRepository.findByBuyerId(userId));
    }
}
