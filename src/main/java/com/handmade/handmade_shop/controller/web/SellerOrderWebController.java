package com.handmade.handmade_shop.controller.web;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.*;
import lombok.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/seller/orders")
@RequiredArgsConstructor
public class SellerOrderWebController {

    private final SellerOrderService sellerOrderService;

    @GetMapping
    public String viewSoldOrders(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<SellerOrderView> orders = sellerOrderService.getOrdersSoldBySeller(userDetails.getUsername());
        model.addAttribute("orders", orders);
        return "seller/order-list";
    }

    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable UUID id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        SellerOrderDetail detail = sellerOrderService.getOrderDetailForSeller(id, userDetails.getUsername());
        model.addAttribute("order", detail);
        return "seller/order-detail";
    }
}

