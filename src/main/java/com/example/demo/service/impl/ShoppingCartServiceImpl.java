package com.example.demo.service.impl;

import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.CartItemUpdateDto;
import com.example.demo.dto.CreateCartItemRequestDto;
import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto findByUserId(Long userId) {
        return shoppingCartMapper.toDto(getShoppingCartByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToCart(Long userId,
                                         CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: "
                        + requestDto.getBookId()));

        CartItem cartItem = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId())
                .map(item -> {
                    item.setQuantity(item.getQuantity() + requestDto.getQuantity());
                    return item;
                })
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setShoppingCart(shoppingCart);
                    newItem.setBook(book);
                    newItem.setQuantity(requestDto.getQuantity());
                    return newItem;
                });

        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public CartItemDto updateQuantity(Long userId, Long cartItemId,
                                      CartItemUpdateDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .filter(item -> item.getShoppingCart().getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id: "
                        + cartItemId));

        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void deleteCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .filter(item -> item.getShoppingCart().getUser().getId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id: "
                        + cartItemId));
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find shopping cart for user id: "
                        + userId));
    }
}
