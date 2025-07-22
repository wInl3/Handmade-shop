package com.handmade.handmade_shop.controller.api;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //User đánh giá sản phẩm đã mua
    @PostMapping
    public ResponseEntity<Void> submit(@RequestBody ReviewRequest request,
                                       Authentication authentication) {
        reviewService.submitReview(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    //Hiển thị đánh giá theo sản phẩm
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(@RequestParam UUID productId) {
        List<ReviewResponse> list = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(list);
    }


    //POST /reviews
    //http://localhost:8080/reviews

    //{
    //  "productId": "640b173d-5ec3-4860-84a3-090828c3d5ae",
    //  "rating": 4,
    //  "comment": "Sản phẩm đẹp, chất lượng tốt"
    //}


    //GET http://localhost:8080/reviews?productId=640b173d-5ec3-4860-84a3-090828c3d5ae
}

