package com.handmade.handmade_shop.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private UUID cartId;
    private String username;
    private List<CartItemResponse> items;
    private BigDecimal totalCartValue;
}

