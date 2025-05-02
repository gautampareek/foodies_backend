package com.musafir.foodApi.service;

import com.musafir.foodApi.entity.UserEntity;
import com.musafir.foodApi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {
    private final UserRepo repo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      UserEntity user = repo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return new User(user.getEmail(),user.getPassword(), Collections.emptyList());
    }
}
