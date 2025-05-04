package com.musafir.foodApi.service;

import com.musafir.foodApi.DTO.OrderRequest;
import com.musafir.foodApi.DTO.OrderResponse;
import com.razorpay.RazorpayException;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse createOrderWithPayment(OrderRequest request);
    void verifyPayment(Map<String,String> paymentData,String status);
    List<OrderResponse> getOrdersOfUser();
    void removeOrder(String orderId);
    List<OrderResponse> getAllOrders();
    void updateOrderStatus(String orderId,String status);
}
