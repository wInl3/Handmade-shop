package com.handmade.handmade_shop.controller.api;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(cartService.getCurrentCart(email));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody CartItemRequest request,
                                          Authentication authentication) {
        cartService.addToCart(authentication.getName(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@RequestBody CartItemRequest request,
                                               Authentication authentication) {
        cartService.removeFromCart(authentication.getName(), request.getProductId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateCartItem(@RequestBody CartItemRequest request,
                                               Authentication authentication) {
        cartService.updateCartItem(authentication.getName(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    //http://localhost:8080/cart/add
    //http://localhost:8080/cart/update
    //http://localhost:8080/cart/remove
    //{
    //  "productId": "ab12cd34-ef56-7890-abcd-123456789001",
    //  "quantity": 5
    //}

}


