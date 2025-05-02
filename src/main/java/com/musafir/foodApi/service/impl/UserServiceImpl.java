package com.musafir.foodApi.service.impl;

import com.musafir.foodApi.DTO.UserRequest;
import com.musafir.foodApi.DTO.UserResponse;
import com.musafir.foodApi.entity.UserEntity;
import com.musafir.foodApi.repo.UserRepo;
import com.musafir.foodApi.service.AuthenticationFacade;
import com.musafir.foodApi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;


    @Override
    public UserResponse registerUser(UserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        UserEntity newUser = modelMapper.map(request,UserEntity.class);
        UserEntity savedUser = userRepo.save(newUser);
        return modelMapper.map(savedUser,UserResponse.class);
    }

    @Override
    public UserEntity findByUserId() {
       String loggedUserEmail = authenticationFacade.getAuthentication().getName();
       log.info("logged user email {}",loggedUserEmail);
       return userRepo.findByEmail(loggedUserEmail).orElseThrow(() -> new RuntimeException("Encounterd issue while finding user"));
    }
}
