package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.exception.ResourceNotFoundException;
import com.handmade.handmade_shop.repository.*;
import com.handmade.handmade_shop.service.*;
import lombok.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SellerOrderServiceImpl implements SellerOrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public List<SellerOrderView> getOrdersSoldBySeller(String sellerEmail) {
        User seller = userRepository.findUserByEmail(sellerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Seller not found"));

        List<Order> orders = orderRepository.findAll().stream()
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> item.getProduct().getSeller().getId().equals(seller.getId())))
                .toList();

        return orders.stream()
                .map(order -> SellerOrderView.builder()
                        .orderId(order.getId())
                        .customerEmail(order.getUser().getEmail())
                        .totalPrice(order.getTotalPrice())
                        .orderDate(order.getOrderDate())
                        .status(order.getStatus())
                        .build())
                .toList();
    }

    @Override
    public SellerOrderDetail getOrderDetailForSeller(UUID orderId, String sellerEmail) {
        User seller = userRepository.findUserByEmail(sellerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Seller not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        boolean hasProduct = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getSeller().getId().equals(seller.getId()));
        if (!hasProduct) {
            throw new AccessDeniedException("You don’t have permission to view this order");
        }

        List<OrderItemResponse> items = order.getItems().stream()
                .filter(item -> item.getProduct().getSeller().getId().equals(seller.getId()))
                .map(item -> OrderItemResponse.builder()
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .toList();

        return SellerOrderDetail.builder()
                .orderId(order.getId())
                .customerEmail(order.getUser().getEmail())
                .shippingAddress(order.getShippingAddress())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .items(items)
                .build();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(item ->
                OrderItemResponse.builder()
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build()
        ).toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .customerEmail(order.getUser().getEmail())
                .totalPrice(order.getTotalPrice())
                .shippingAddress(order.getShippingAddress())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .items(items)
                .build();
    }
}

