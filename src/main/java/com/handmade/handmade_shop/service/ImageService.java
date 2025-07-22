package com.handmade.handmade_shop.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageService {

    String uploadImage(UUID productId, MultipartFile file, String sellerEmail) throws IOException;

}
