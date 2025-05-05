package com.musafir.foodApi.service.impl;

import com.musafir.foodApi.DTO.OrderRequest;
import com.musafir.foodApi.DTO.OrderResponse;
import com.musafir.foodApi.entity.CartEntity;
import com.musafir.foodApi.entity.OrderEntity;
import com.musafir.foodApi.repo.CartRepo;
import com.musafir.foodApi.repo.OrderRepo;
import com.musafir.foodApi.service.AuthenticationFacade;
import com.musafir.foodApi.service.OrderService;
import com.musafir.foodApi.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CartRepo cartRepo;

    @Value("${razorpay.key}")
    private String RAZORPAY_KEY;
    @Value("${razorpay.secret}")
    private String RAZORPAY_SECRET;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) {
        log.info("recieved order request {}",request );
        OrderEntity requestOrder = modelMapper.map(request, OrderEntity.class);
        OrderEntity savedOrder = orderRepo.save(requestOrder);
        //payment part here

        RazorpayClient client = null;
        try {
            client = new RazorpayClient(RAZORPAY_KEY,RAZORPAY_SECRET);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(savedOrder.getAmount() * 100));
        orderRequest.put("currency","INR");
        orderRequest.put("payment_capture",1);

        Order razorOrder = client.orders.create(orderRequest);
        savedOrder.setRazorPayOrderId(razorOrder.get("id"));
        } catch (RazorpayException e) {
            System.out.println(e.getMessage());
        }
        String loggedInUser = userService.findByUserId().getEmail();
        savedOrder.setUserId(loggedInUser);
        return modelMapper.map(orderRepo.save(savedOrder),OrderResponse.class);
        
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData,String status) {
        String razorPayOrderID = paymentData.get("razorpay_order_id");
        OrderEntity existingOrder = orderRepo.findByRazorPayOrderId(razorPayOrderID)
                .orElseThrow(()->new RuntimeException("Order not found"));
        existingOrder.setRazorPaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorPayPaymentId(paymentData.get("razorpay_payment_id"));
        orderRepo.save(existingOrder);
        if("paid".equals(status)){
            cartRepo.deleteByUserId(existingOrder.getUserId());
        }
    }

    @Override
    public List<OrderResponse> getOrdersOfUser() {
        String loggedInUser = userService.findByUserId().getEmail();
       List<OrderEntity> allOrders =  orderRepo.findByUserId(loggedInUser);
       return allOrders.stream().map(order -> modelMapper.map(order,OrderResponse.class))
               .toList();
    }

    @Override
    public void removeOrder(String orderId) {
    orderRepo.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderEntity> allOrders = orderRepo.findAll();
        return allOrders.stream().map(order -> modelMapper.map(order,OrderResponse.class))
                .toList();
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        OrderEntity order = orderRepo.findById(orderId).orElseThrow(()->new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        orderRepo.save(order);
    }

}
