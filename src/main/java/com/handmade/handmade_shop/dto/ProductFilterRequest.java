package com.handmade.handmade_shop.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductFilterRequest {
    private UUID categoryId;
    private UUID sellerId;
    private Double minPrice;
    private Double maxPrice;
    private String keyword;
    private String sortBy = "createdAt"; // mặc định sort theo thời gian tạo
    private String sortDir = "desc";     // mặc định giảm dần
    private int page = 0;
    private int size = 10;
}
