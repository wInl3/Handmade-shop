package com.handmade.handmade_shop.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    private String status; // sẽ parse thành OrderStatus enum
}

