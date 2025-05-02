package com.musafir.foodApi.service.impl;

import com.musafir.foodApi.DTO.CartRequest;
import com.musafir.foodApi.DTO.CartResponse;
import com.musafir.foodApi.entity.CartEntity;
import com.musafir.foodApi.repo.CartRepo;
import com.musafir.foodApi.service.CartService;
import com.musafir.foodApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final UserService userService;
    private final ModelMapper mapper;

    @Override
    public CartResponse addToCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId().getEmail();
        //check if cart is already there for user
        CartEntity cart = cartRepo.findByUserId(loggedInUserId).orElseGet(()-> new CartEntity(loggedInUserId));
        //check if this particular item is already there in cart
        Map<String, Integer> items = cart.getItems();
        items.put(request.getFoodId(), items.getOrDefault(request.getFoodId(),0)+1);
        cart.setItems(items);
        return mapper.map(cartRepo.save(cart),CartResponse.class);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId = userService.findByUserId().getEmail();
        CartEntity cart = cartRepo.findByUserId(loggedInUserId).orElseGet(()-> new CartEntity(loggedInUserId));
        return mapper.map(cart,CartResponse.class);
    }

    @Override
    public void deleteCart() {
        String loggedInUserId = userService.findByUserId().getEmail();
        cartRepo.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeFromCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId().getEmail();
        CartEntity cart = cartRepo.findByUserId(loggedInUserId).orElseThrow(() -> new RuntimeException("Cart not found"));
        Map<String, Integer> items = cart.getItems();
        String foodId = request.getFoodId();
        int currentQty = items.get(foodId);
        if(currentQty > 1)
            items.put(foodId,currentQty-1);
        else
            items.remove(foodId);
        cart.setItems(items);
        return mapper.map(cartRepo.save(cart),CartResponse.class);

    }
}
