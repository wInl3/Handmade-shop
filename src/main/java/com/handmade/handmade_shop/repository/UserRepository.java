package com.handmade.handmade_shop.repository;

import com.handmade.handmade_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByFullName(String name);

    Optional<User> findUserByEmail(String userEmail);

    Optional<User> findByUsername(String username);

}
