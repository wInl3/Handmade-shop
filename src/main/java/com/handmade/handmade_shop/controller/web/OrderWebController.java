package com.handmade.handmade_shop.controller.web;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderWebController {

    private final OrderService orderService;

    @GetMapping("/checkout")
    public String showCheckoutForm(Model model) {
        model.addAttribute("orderRequest", new OrderRequest());
        return "order/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@ModelAttribute OrderRequest orderRequest,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        try {
            OrderResponse response = orderService.checkout(userDetails.getUsername(), orderRequest);
            return "redirect:/payment/" + response.getOrderId(); // ✅ chuyển sang trang thanh toán
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "order/checkout";
        }
    }

    @GetMapping("/my-orders")
    public String viewMyOrders(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<OrderResponse> orders = orderService.getOrdersByUser(userDetails.getUsername());
        model.addAttribute("orders", orders);
        return "order/order-list";
    }

    @GetMapping("/my-orders/{id}")
    public String viewOrderDetail(@PathVariable UUID id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        OrderResponse order = orderService.getOrderById(userDetails.getUsername(), id);
        model.addAttribute("order", order);
        return "order/order-detail";
    }

    @PostMapping("/my-orders/{id}/update-status")
    public String updateOrderStatus(@PathVariable UUID id,
                                    @RequestParam String status,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        orderService.updateOrderStatus(id, status); // logic đã có sẵn
        return "redirect:/orders/my-orders/" + id;
    }


}
