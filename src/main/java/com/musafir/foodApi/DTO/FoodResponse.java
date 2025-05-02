package com.musafir.foodApi.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FoodResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
}
