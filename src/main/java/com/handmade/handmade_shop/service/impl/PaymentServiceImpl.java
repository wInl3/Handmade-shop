package com.handmade.handmade_shop.service.impl;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.entity.*;
import com.handmade.handmade_shop.enums.*;
import com.handmade.handmade_shop.exception.ResourceNotFoundException;
import com.handmade.handmade_shop.repository.*;
import com.handmade.handmade_shop.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse payOrder(String userEmail, PaymentRequest request) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Bạn không thể thanh toán cho đơn hàng người khác");
        }

        if (paymentRepository.findByOrder(order).isPresent()) {
            throw new ResourceNotFoundException("Đơn hàng đã được thanh toán");
        }

        PaymentMethod method = PaymentMethod.valueOf(request.getMethod().toUpperCase());

        PaymentStatus status = method == PaymentMethod.COD ? PaymentStatus.PENDING : PaymentStatus.PAID;

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalPrice())
                .method(method)
                .paymentStatus(status)
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(order.getId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getPaymentStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }
}

