package com.handmade.handmade_shop.service;

import com.handmade.handmade_shop.dto.*;

import java.util.*;

public interface ReviewService {
    void submitReview(String userEmail, ReviewRequest request);
    List<ReviewResponse> getReviewsByProduct(UUID productId);
}
