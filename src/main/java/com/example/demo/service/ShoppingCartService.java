package com.example.demo.service;

import com.example.demo.dto.CartItemUpdateDto;
import com.example.demo.dto.CreateCartItemRequestDto;
import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;

public interface ShoppingCartService {
    ShoppingCartDto findByUserId(Long userId);

    ShoppingCartDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateQuantity(Long userId, Long cartItemId, CartItemUpdateDto requestDto);

    void deleteCartItem(Long userId, Long cartItemId);

    void registerNewShoppingCart(User user);

    void clear(ShoppingCart shoppingCart);
}
