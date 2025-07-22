package com.handmade.handmade_shop.dto;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private UUID productId;
    private int rating;
    private String comment;
}

