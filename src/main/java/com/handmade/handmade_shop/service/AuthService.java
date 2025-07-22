package com.handmade.handmade_shop.service;

import com.handmade.handmade_shop.config.JwtUtil;
import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.exception.ResourceNotFoundException;
import com.handmade.handmade_shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public AuthResponse authenticate(AuthRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            return new AuthResponse(token);

        } catch (Exception e) {
            e.printStackTrace(); // <--- IN RA LOG
            throw new RuntimeException("Invalid username or password");
        }
    }



    public void register(RegisterRequest req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());

        //mã hoá password trước khi lưu
        user.setPassword(encoder.encode(req.getPassword())); // dùng BCryptPasswordEncoder
        user.setCreatedAt(LocalDate.now());

        Role customerRole = roleRepo.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(customerRole);

        userRepo.save(user);
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepo.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Current password is incorrect");
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepo.save(user);
    }
}
