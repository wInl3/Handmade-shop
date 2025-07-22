package com.handmade.handmade_shop.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private UUID orderId;
    private String method; // COD, BANK_TRANSFER, PAYPAL
}
