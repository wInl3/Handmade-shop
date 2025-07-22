package com.handmade.handmade_shop.controller.api;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Checkout đơn hàng từ giỏ hàng
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@RequestBody OrderRequest request,
                                                  Authentication authentication) {
        String email = authentication.getName();
        OrderResponse response = orderService.checkout(email, request);
        return ResponseEntity.ok(response);
    }

    //Xem danh sách đơn hàng (GET /orders)
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(Authentication authentication) {
        String email = authentication.getName();
        List<OrderResponse> orders = orderService.getOrdersByUser(email);
        return ResponseEntity.ok(orders);
    }

    //Xem chi tiết đơn hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id,
                                                      Authentication authentication) {
        String email = authentication.getName();
        OrderResponse response = orderService.getOrderById(email, id);
        return ResponseEntity.ok(response);
    }

    //cập nhật trạng thái đơn hàng cho ADMIN hoặc SELLER
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<Void> updateStatus(@PathVariable UUID id,
                                             @RequestBody UpdateOrderStatusRequest request) {
        orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok().build();
    }


    //POST http://localhost:8080/orders/checkout
    //GET http://localhost:8080/orders
    //GET http://localhost:8080/orders/{orderId}
    //PUT http://localhost:8080/orders/fdb25cd0-87cf-4474-8182-c743966387b0/status

    //{
    //  "status": "SHIPPED"
    //}
}

