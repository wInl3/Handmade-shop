package com.handmade.handmade_shop.repository;

import com.handmade.handmade_shop.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {

}
