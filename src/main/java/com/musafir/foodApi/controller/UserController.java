package com.musafir.foodApi.controller;

import com.musafir.foodApi.DTO.UserRequest;
import com.musafir.foodApi.DTO.UserResponse;
import com.musafir.foodApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Slf4j
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest request){
        log.info("recieved request to add user {}",request);
    UserResponse response = userService.registerUser(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
