package com.musafir.foodApi.controller;

import com.musafir.foodApi.DTO.AuthRequest;
import com.musafir.foodApi.DTO.AuthResponse;
import com.musafir.foodApi.service.AppUserDetailService;
import com.musafir.foodApi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager manager;
    private final AppUserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AuthResponse login(@RequestBody AuthRequest request){
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail()
                ,request.getPassword()));
        final UserDetails userDetails = userDetailService.loadUserByUsername(request.getEmail());
        final String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(request.getEmail(), token);

    }
}
