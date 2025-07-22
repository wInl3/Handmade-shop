package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.enums.*;
import com.handmade.handmade_shop.exception.ResourceNotFoundException;
import com.handmade.handmade_shop.repository.*;
import com.handmade.handmade_shop.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse checkout(String userEmail, OrderRequest request) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty!");
        }

        // Tính tổng
        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .totalPrice(total)
                .shippingAddress(request.getShippingAddress())
                .status(OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            // ✅ Kiểm tra và cập nhật tồn kho
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ số lượng!");
            }
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            orderItemRepository.save(orderItem);

            itemResponses.add(OrderItemResponse.builder()
                    .productId(product.getProductId())
                    .productName(product.getName())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(product.getPrice())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .build());
        }

        // ✅ Cập nhật toàn bộ sản phẩm đã thay đổi tồn kho
        productRepository.saveAll(cartItems.stream().map(CartItem::getProduct).toList());

        // Xoá giỏ hàng sau khi checkout
        cartItemRepository.deleteAll(cartItems); // Xoá trong DB
        cart.getItems().clear();                 // Clear bộ nhớ
        cartRepository.save(cart);              // Cập nhật lại cart


        return OrderResponse.builder()
                .orderId(order.getId())
                .customerEmail(user.getEmail())
                .totalPrice(total)
                .shippingAddress(order.getShippingAddress())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .items(itemResponses)
                .build();
    }

    @Override
    public List<OrderResponse> getOrdersByUser(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream().map(order -> {
            List<OrderItemResponse> items = order.getItems().stream().map(item -> {
                return OrderItemResponse.builder()
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build();
            }).toList();

            return OrderResponse.builder()
                    .orderId(order.getId())
                    .customerEmail(user.getEmail())
                    .totalPrice(order.getTotalPrice())
                    .shippingAddress(order.getShippingAddress())
                    .status(order.getStatus())
                    .orderDate(order.getOrderDate())
                    .items(items)
                    .build();
        }).toList();
    }

    @Override
    public OrderResponse getOrderById(String userEmail, UUID orderId) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Bạn không có quyền xem đơn hàng này");
        }

        List<OrderItemResponse> items = order.getItems().stream().map(item -> {
            Product product = item.getProduct();
            return OrderItemResponse.builder()
                    .productId(product.getProductId())
                    .productName(product.getName())
                    .quantity(item.getQuantity())
                    .unitPrice(item.getUnitPrice())
                    .totalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .build();
        }).toList();

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

    @Override
    @Transactional
    public void updateOrderStatus(UUID orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus status;
        try {
            status = OrderStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException("Invalid status: " + newStatus);
        }

        order.setStatus(status);
        orderRepository.save(order);
    }


}

