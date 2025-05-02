package com.musafir.foodApi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FoodConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
