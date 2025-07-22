package com.handmade.handmade_shop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse { //thông tin ảnh, bao gồm thumbnail
    private String imageUrl;
    private boolean isThumbnail;
}
