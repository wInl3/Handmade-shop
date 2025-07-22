package com.handmade.handmade_shop.controller.web;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartWebController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        CartResponse cart = cartService.getCurrentCart(userDetails.getUsername());
        model.addAttribute("cart", cart);
        return "cart/view-cart"; // Thymeleaf view
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam UUID productId,
                            @RequestParam int quantity,
                            @AuthenticationPrincipal UserDetails userDetails) {
        cartService.addToCart(userDetails.getUsername(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam UUID productId,
                                 @RequestParam int quantity,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        cartService.updateCartItem(userDetails.getUsername(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam UUID productId,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        cartService.removeFromCart(userDetails.getUsername(), productId);
        return "redirect:/cart";
    }
}

