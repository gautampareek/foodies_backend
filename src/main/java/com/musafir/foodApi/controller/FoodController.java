package com.musafir.foodApi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musafir.foodApi.DTO.FoodRequest;
import com.musafir.foodApi.DTO.FoodResponse;
import com.musafir.foodApi.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
@CrossOrigin("*")
public class FoodController {
    private final FoodService foodService;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable("id") String id){
        foodService.deleteFood(id);
    }
    @GetMapping("/byId")
    public ResponseEntity<FoodResponse> getAllFood(@RequestParam("id") String id){
        return ResponseEntity.ok(foodService.readFoods(id));
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> getAllFoods(){
        return ResponseEntity.ok(foodService.readFoods());
    }
    @PostMapping
    public ResponseEntity<FoodResponse> addFood(
            @RequestPart("food") String foodString,
            @RequestPart("file")MultipartFile file
    ){
        log.info("Recieved food request to add food");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.debug("parsing food JSON String");
            FoodRequest request = objectMapper.readValue(foodString, FoodRequest.class);
            FoodResponse foodResponse = foodService.addFood(request,file);
            log.info("Food item added Successfully");
            return ResponseEntity.ok(foodResponse);

        }catch (JsonProcessingException ex){
            log.error("Invalid JSON format recieved {}",ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid JSON Format");
        }

    }
}
