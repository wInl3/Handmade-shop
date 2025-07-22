package com.handmade.handmade_shop.service;

import com.handmade.handmade_shop.dto.*;

public interface PaymentService {
    PaymentResponse payOrder(String userEmail, PaymentRequest request);
}

