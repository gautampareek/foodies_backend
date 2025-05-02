package com.musafir.foodApi.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FoodRequest {
    private String name;
    private String description;
    private double price;
    private String category;
}
