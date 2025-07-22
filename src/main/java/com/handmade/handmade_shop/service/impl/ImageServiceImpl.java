package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.exception.ResourceNotFoundException;
import com.handmade.handmade_shop.repository.*;
import com.handmade.handmade_shop.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final ProductRepository productRepository;

    @Override
    public String uploadImage(UUID productId, MultipartFile file, String sellerEmail) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getSeller().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("You do not have permission to upload image for this product");
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.matches("(?i).+\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("Only image files (JPG, PNG, GIF) are allowed.");
        }

        // Save file
        String fileName = UUID.randomUUID() + "_" + originalFilename;
        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir);
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Image image = Image.builder()
                .imageUrl("/uploads/" + fileName)
                .product(product)
                .isThumbnail(false)
                .createdAt(LocalDateTime.now())
                .build();

        imageRepository.save(image);
        return image.getImageUrl();
    }



}
