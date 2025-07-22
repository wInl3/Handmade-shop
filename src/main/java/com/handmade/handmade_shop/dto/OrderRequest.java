package com.handmade.handmade_shop.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String fullName;
    private String phone;
    private String shippingAddress;
}


