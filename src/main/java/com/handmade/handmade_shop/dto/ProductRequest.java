package com.handmade.handmade_shop.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private UUID categoryId;
    private List<String> imageUrls = new ArrayList<>();// URL hoặc base64, hoặc xử lý qua Multipart sau
    private int thumbnailIndex;     // Chỉ định ảnh nào là thumbnail (theo thứ tự list)

    // ✅ Thêm phần dưới để upload file
    private List<MultipartFile> images;
}