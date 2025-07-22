package com.handmade.handmade_shop.repository;

import com.handmade.handmade_shop.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
