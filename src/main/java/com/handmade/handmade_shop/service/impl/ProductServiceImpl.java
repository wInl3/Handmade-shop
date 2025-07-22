package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.exception.ResourceNotFoundException;
import com.handmade.handmade_shop.repository.*;
import com.handmade.handmade_shop.repository.custom.ProductRepositoryCustom;
import com.handmade.handmade_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;

    private final ProductRepositoryCustom productRepositoryCustom;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              UserRepository userRepository,
                              ImageRepository imageRepository,
                              ReviewRepository reviewRepository,
                              @Qualifier("productRepositoryCustomImpl") ProductRepositoryCustom productRepositoryCustom) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.reviewRepository = reviewRepository;
        this.productRepositoryCustom = productRepositoryCustom;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request, String sellerEmail) {
        User seller = userRepository.findUserByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(category)
                .seller(seller)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        Product saved = productRepository.save(product);

        List<Image> images = new ArrayList<>();
        for (int i = 0; i < request.getImageUrls().size(); i++) {
            String url = request.getImageUrls().get(i);
            boolean isThumbnail = (i == request.getThumbnailIndex());

            Image image = Image.builder()
                    .imageUrl(url)
                    .isThumbnail(isThumbnail)
                    .createdAt(LocalDateTime.now())
                    .product(saved)
                    .build();
            images.add(image);
        }
        imageRepository.saveAll(images);
        saved.setImages(images);

        return toResponse(saved);
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductResponse response = convertToResponse(product);

        // Lấy danh sách review
        List<ReviewResponse> reviews = reviewRepository.findByProductId(id)
                .stream()
                .map(this::convertToReviewResponse)
                .toList();
        response.setReviews(reviews);

        return response;
    }


    private ReviewResponse convertToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .username(review.getUser().getFullName()) // ✅ đúng tên field
                .build();
    }

    @Override
    public void deleteProduct(UUID productId, String sellerEmail) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getEmail().equals(sellerEmail)) {
            throw new ResourceNotFoundException("Access denied: You are not the seller of this product");
        }

        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getByCategory(UUID categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getBySeller(UUID sellerId) {
        return productRepository.findBySeller_Id(sellerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(UUID id, ProductRequest request, String sellerEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getSeller().getEmail().equals(sellerEmail)) {
            throw new AccessDeniedException("You do not have permission to update this product");
        }

        // Cập nhật các thuộc tính từ request
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());

        // Nếu có category
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        return ProductResponse.from(productRepository.save(product));
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .isActive(product.isActive())
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

    @Override
    public Page<ProductResponse> filterProducts(ProductFilterRequest filter) {
        Page<Product> productPage = productRepositoryCustom.filterProducts(filter);
        List<ProductResponse> responseList = productPage.getContent()
                .stream()
                .map(ProductResponse::fromEntity)// giả sử bạn có mapper
                .toList();

        return new PageImpl<>(responseList, productPage.getPageable(), productPage.getTotalElements());
    }


    @Override
    public ProductResponse convertToResponse(Product product) {
        return toResponse(product);
    }

    @Override
    public UUID getSellerIdByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"))
                .getId();
    }

}
