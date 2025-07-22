package com.handmade.handmade_shop.controller.api;

import com.handmade.handmade_shop.dto.*;
import com.handmade.handmade_shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest request,
                                               Authentication authentication) {
        String email = authentication.getName();
        PaymentResponse response = paymentService.payOrder(email, request);
        return ResponseEntity.ok(response);
    }


    //POST /payments/pay
    //http://localhost:8080/payments/pay
    //{
    //  "orderId": "fdb25cd0-87cf-4474-8182-c743966387b0",
    //  "method": "BANK_TRANSFER"
    //}

    // Lưu ý:
    //Nếu chọn "method": "COD" → paymentStatus = PENDING
    //
    //Nếu "BANK_TRANSFER" hoặc "PAYPAL" → paymentStatus = PAID

}

