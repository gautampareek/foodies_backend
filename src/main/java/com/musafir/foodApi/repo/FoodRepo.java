package com.musafir.foodApi.repo;

import com.musafir.foodApi.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepo extends MongoRepository<FoodEntity,String> {
}
