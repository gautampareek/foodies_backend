package com.musafir.foodApi.service;

import com.musafir.foodApi.DTO.FoodRequest;
import com.musafir.foodApi.DTO.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {
    String uploadFile(MultipartFile multipartFile);
    FoodResponse addFood(FoodRequest foodRequest, MultipartFile file);
    List<FoodResponse> readFoods();
    FoodResponse readFoods(String id);
    boolean deleteFile(String name);
    void deleteFood(String id);
}
