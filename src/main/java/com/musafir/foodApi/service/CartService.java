package com.musafir.foodApi.service;

import com.musafir.foodApi.DTO.CartRequest;
import com.musafir.foodApi.DTO.CartResponse;

public interface CartService {
    CartResponse addToCart(CartRequest request);
    CartResponse getCart();
    void deleteCart();
    CartResponse removeFromCart(CartRequest request);
    CartResponse deleteFullItemFromCart(String foodId);

}
