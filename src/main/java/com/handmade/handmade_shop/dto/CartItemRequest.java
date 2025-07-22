package com.handmade.handmade_shop.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private UUID productId;
    private int quantity;
}

