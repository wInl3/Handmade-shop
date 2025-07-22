package com.handmade.handmade_shop.repository.custom;

import com.handmade.handmade_shop.dto.ProductFilterRequest;
import com.handmade.handmade_shop.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {
    Page<Product> filterProducts(ProductFilterRequest filter);
}
