package com.musafir.foodApi.repo;

import com.musafir.foodApi.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<UserEntity,String> {
   Optional<UserEntity> findByEmail(String email);
}
