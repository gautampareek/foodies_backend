package com.musafir.foodApi.service;

import com.musafir.foodApi.DTO.UserRequest;
import com.musafir.foodApi.DTO.UserResponse;
import com.musafir.foodApi.entity.UserEntity;

public interface UserService {
   UserResponse registerUser(UserRequest request);
   UserEntity findByUserId();
}
