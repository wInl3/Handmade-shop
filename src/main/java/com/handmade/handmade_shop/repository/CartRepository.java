package com.handmade.handmade_shop.repository;

import com.handmade.handmade_shop.entity.Cart;
import com.handmade.handmade_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(User user);

}
