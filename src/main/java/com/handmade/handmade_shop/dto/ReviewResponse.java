package com.handmade.handmade_shop.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private UUID reviewId;
    private String username;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
