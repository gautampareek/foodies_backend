package com.musafir.foodApi.controller;

import com.musafir.foodApi.DTO.OrderRequest;
import com.musafir.foodApi.DTO.OrderResponse;
import com.musafir.foodApi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request){
        System.out.println("this is the request"+request);
        OrderResponse orderWithPayment = orderService.createOrderWithPayment(request);
        return new ResponseEntity<>(orderWithPayment, HttpStatus.OK);
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOrder(@RequestBody Map<String,String> paymentData){
        System.out.println("This the verify request"+paymentData);
        orderService.verifyPayment(paymentData,"paid");
        return ResponseEntity.ok("Verified");
    }
    @GetMapping
    public List<OrderResponse> getOrders(){
       return orderService.getOrdersOfUser();
    }
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String orderId){
    //delete order call
        orderService.removeOrder(orderId);
    }
    @GetMapping("/all")
    public List<OrderResponse> getOrdersOfAllUsers(){
    //get all orders call
        return orderService.getAllOrders();
    }
    @PatchMapping("/status/{orderId}")
    public void updateOrderStatus(@PathVariable String orderId,@RequestParam String status){
        orderService.updateOrderStatus(orderId,status);
    }
}
