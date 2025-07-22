package com.handmade.handmade_shop.service;


import com.handmade.handmade_shop.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(UUID id);
    User createUser(User user);
    void deleteUser(UUID id);

}
