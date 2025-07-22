package com.handmade.handmade_shop.dto;


import com.handmade.handmade_shop.entity.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse { //dùng để trả về thông tin chi tiết sản phẩm
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private boolean isActive;
    private int soldCount;
    private String categoryName;
    private UUID sellerId;
    private UUID categoryId;
    private String sellerName;
    private List<ImageResponse> images;
    private List<ReviewResponse> reviews;

    public static ProductResponse from(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setQuantity(product.getQuantity());
        response.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        return response;
    }

    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .soldCount(product.getSoldCount())
                .categoryName(product.getCategory().getName())
                .sellerId(product.getSeller().getId())
                .sellerName(product.getSeller().getFullName())
                .images(product.getImages().stream().map(img -> ImageResponse.builder()
                        .imageUrl(img.getImageUrl())
                        .isThumbnail(img.isThumbnail())
                        .build()).toList())
                .build();
    }



}