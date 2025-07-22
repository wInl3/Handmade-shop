package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.entity.User;
import com.handmade.handmade_shop.repository.UserRepository;
import com.handmade.handmade_shop.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    //ham check login
    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        User user;

        if (input.contains("@")) {
            user = userRepo.findUserByEmail(input)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        } else {
            user = userRepo.findByUsername(input)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        }
        System.out.println("looo");
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }


}
