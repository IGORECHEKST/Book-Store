package com.example.demo.service;

import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.CartItemUpdateDto;
import com.example.demo.dto.CreateCartItemRequestDto;
import com.example.demo.dto.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto findByUserId(Long userId);

    ShoppingCartDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto);

    CartItemDto updateQuantity(Long userId, Long cartItemId, CartItemUpdateDto requestDto);

    void deleteCartItem(Long userId, Long cartItemId);
}
