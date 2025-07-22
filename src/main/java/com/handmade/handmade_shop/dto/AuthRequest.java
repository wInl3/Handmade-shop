package com.handmade.handmade_shop.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthRequest {
    private String username;
    private String password;
}