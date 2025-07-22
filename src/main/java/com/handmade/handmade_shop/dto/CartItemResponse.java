package com.handmade.handmade_shop.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private UUID productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}

