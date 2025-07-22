package com.handmade.handmade_shop.service;


import com.handmade.handmade_shop.dto.CartResponse;

import java.util.UUID;

public interface CartService {

    CartResponse getCurrentCart(String userEmail);
    void addToCart(String userEmail, UUID productId, int quantity);
    void removeFromCart(String userEmail, UUID productId);
    void updateCartItem(String userEmail, UUID productId, int newQuantity);

}
