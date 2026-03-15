package com.ordermgtsystem.service;

import com.ordermgtsystem.dto.OrderCreateRequest;
import com.ordermgtsystem.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderCreateRequest request);

    default OrderResponse createOrder(OrderCreateRequest request) {
        return placeOrder(request);
    }
}