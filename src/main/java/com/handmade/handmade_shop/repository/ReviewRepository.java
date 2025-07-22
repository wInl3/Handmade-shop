package com.handmade.handmade_shop.repository;

import com.handmade.handmade_shop.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    //Nếu @Id là productId, thì bạn phải dùng product.productId, không dùng product.id
    @Query("SELECT r FROM Review r WHERE r.product.productId = :productId")
    List<Review> findByProductId(@Param("productId") UUID productId);


}
