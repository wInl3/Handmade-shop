package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.repository.*;
import com.handmade.handmade_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public void submitReview(String userEmail, ReviewRequest request) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByProduct(UUID productId) {
        return reviewRepository.findByProductId(productId).stream().map(r -> ReviewResponse.builder()
                .reviewId(r.getId())
                .username(r.getUser().getUsername())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build()).toList();
    }
}

