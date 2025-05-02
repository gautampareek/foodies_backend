package com.musafir.foodApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String id;
    private String userId;
    private Map<String,Integer> items = new HashMap<>();
}
