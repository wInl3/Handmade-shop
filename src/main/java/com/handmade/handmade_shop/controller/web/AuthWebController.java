package com.handmade.handmade_shop.controller.web;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth/web")  // đổi path prefix riêng
public class AuthWebController {

    private final AuthService authService;

    public AuthWebController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email,
                              @RequestParam String password,
                              Model model,
                              HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.authenticate(
                    new AuthRequest(email, password)
            );

            // Lưu token vào cookie
            Cookie cookie = new Cookie("token", authResponse.getAccessToken());
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", "Sai email hoặc mật khẩu");
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String fullName,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String role,
                                 Model model) {
        try {
            RegisterRequest request = RegisterRequest.builder()
                    .fullName(fullName)
                    .email(email)
                    .username(email)
                    .password(password)
                    .role(role)
                    .build();

            authService.register(request);
            return "redirect:/auth/web/login";
        } catch (Exception e) {
            model.addAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            return "auth/register";
        }
    }


}

