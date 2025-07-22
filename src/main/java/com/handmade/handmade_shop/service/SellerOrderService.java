package com.handmade.handmade_shop.service;

import com.handmade.handmade_shop.dto.*;

import java.util.*;

public interface SellerOrderService {
    List<SellerOrderView> getOrdersSoldBySeller(String sellerEmail);
    SellerOrderDetail getOrderDetailForSeller(UUID orderId, String sellerEmail);
}
