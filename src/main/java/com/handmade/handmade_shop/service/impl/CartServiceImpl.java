package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.exception.ResourceNotFoundException;
import com.handmade.handmade_shop.repository.*;
import com.handmade.handmade_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse getCurrentCart(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .items(new ArrayList<>()) // 👈 THÊM nếu cần
                            .build();
                    return cartRepository.save(newCart);
                });


        List<CartItemResponse> itemResponses = cart.getItems().stream().map(item -> {
            Product p = item.getProduct();
            return CartItemResponse.builder()
                    .productId(p.getProductId())
                    .productName(p.getName())
                    .quantity(item.getQuantity())
                    .unitPrice(p.getPrice())
                    .totalPrice(p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .build();
        }).toList();

        BigDecimal total = itemResponses.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getId())
                .username(user.getUsername())
                .items(itemResponses)
                .totalCartValue(total)
                .build();
    }

    @Override
    public void addToCart(String userEmail, UUID productId, int quantity) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(CartItem.builder().cart(cart).product(product).quantity(0).build());

        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
    }

    @Override
    public void removeFromCart(String userEmail, UUID productId) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartItemRepository.findByCartAndProduct(cart, product)
                .ifPresent(cartItemRepository::delete);
    }

    @Override
    public void updateCartItem(String userEmail, UUID productId, int newQuantity) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        item.setQuantity(newQuantity);
        cartItemRepository.save(item);
    }
}

