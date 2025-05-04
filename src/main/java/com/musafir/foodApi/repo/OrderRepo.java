package com.musafir.foodApi.repo;

import com.musafir.foodApi.DTO.OrderItem;
import com.musafir.foodApi.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends MongoRepository<OrderEntity,String> {
    List<OrderEntity> findByUserId(String userId);
    Optional<OrderEntity> findByRazorPayOrderId(String razorPayOrderId);
}
