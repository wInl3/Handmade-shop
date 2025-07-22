package com.handmade.handmade_shop.repository;

import com.handmade.handmade_shop.entity.Product;
import com.handmade.handmade_shop.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
    List<Product> findByCategory_CategoryId(UUID categoryId);

    List<Product> findBySeller_Id(UUID sellerId);

}

