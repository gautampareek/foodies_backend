package com.musafir.foodApi.entity;

import com.musafir.foodApi.DTO.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;
    private String userId;
    private String userAddress;
    private String phoneNumber;
    private String email;
    private List<OrderItem> orderdItems = new ArrayList<>();
    private double amount;
    private String paymentStatus;
    private String razorPayOrderId;
    private String razorPaySignature;
    private String razorPayPaymentId;
    private String orderStatus;

}
