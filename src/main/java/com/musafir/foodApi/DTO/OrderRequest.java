package com.musafir.foodApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String userAddress;
    private List<OrderItem> orderdItems = new ArrayList<>();
    private double amount;
    private String email;
    private String phoneNumber;
    private String orderStatus;
}
