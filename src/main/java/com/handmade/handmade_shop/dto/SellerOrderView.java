package com.handmade.handmade_shop.dto;

import com.handmade.handmade_shop.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerOrderView {
    private UUID orderId;
    private String customerEmail;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private OrderStatus status;
}
