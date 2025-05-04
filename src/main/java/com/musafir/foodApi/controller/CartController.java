package com.musafir.foodApi.controller;

import com.musafir.foodApi.DTO.CartRequest;
import com.musafir.foodApi.DTO.CartResponse;
import com.musafir.foodApi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody CartRequest request){
        String foodId = request.getFoodId();
        if(foodId == null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"FoodId not not in request");
        }
        return ResponseEntity.ok().body(cartService.addToCart(request));
    }
    @GetMapping
    public ResponseEntity<CartResponse> getCart(){
        CartResponse cart = cartService.getCart();
        return ResponseEntity.ok(cart);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(){
        cartService.deleteCart();
    }
    @PostMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestBody CartRequest request){
        return ResponseEntity.ok(cartService.removeFromCart(request));

    }
    @DeleteMapping("/removeItem/{id}")
    public ResponseEntity<CartResponse> removeFullItemFromCart(@PathVariable String id){
        return ResponseEntity.ok(cartService.deleteFullItemFromCart(id));
    }
}
