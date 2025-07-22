package com.handmade.handmade_shop.service;

import com.handmade.handmade_shop.dto.*;

import java.util.*;

public interface OrderService {
    OrderResponse checkout(String userEmail, OrderRequest request);

    List<OrderResponse> getOrdersByUser(String userEmail);

    OrderResponse getOrderById(String userEmail, UUID orderId);

    void updateOrderStatus(UUID orderId, String newStatus);



}

