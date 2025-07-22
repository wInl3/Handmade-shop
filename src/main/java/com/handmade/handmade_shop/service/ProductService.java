package com.handmade.handmade_shop.service;


import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.Product;
import org.springframework.data.domain.Page;

import java.util.*;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request, String sellerEmail);

    ProductResponse getProductById(UUID productId);

    void deleteProduct(UUID productId, String sellerEmail);

    List<ProductResponse> getAll();

    List<ProductResponse> getByCategory(UUID categoryId);

    List<ProductResponse> getBySeller(UUID sellerId);

    ProductResponse updateProduct(UUID productId, ProductRequest request, String sellerEmail);

    Page<ProductResponse> filterProducts(ProductFilterRequest filter);

    ProductResponse convertToResponse(Product product);

    UUID getSellerIdByEmail(String email);


}
