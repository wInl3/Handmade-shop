package com.handmade.handmade_shop.controller.web;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentWebController {

    private final PaymentService paymentService;

    @GetMapping("/{orderId}")
    public String showPaymentForm(@PathVariable UUID orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order/payment";
    }

    @PostMapping
    public String processPayment(@ModelAttribute PaymentRequest request,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        try {
            PaymentResponse response = paymentService.payOrder(userDetails.getUsername(), request);
            model.addAttribute("payment", response);
            return "order/payment-success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "order/payment";
        }
    }
}
