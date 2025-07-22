package com.handmade.handmade_shop.dto;

import com.handmade.handmade_shop.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerOrderDetail {
    private UUID orderId;
    private String customerEmail;
    private String shippingAddress;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private List<OrderItemResponse> items;
}
