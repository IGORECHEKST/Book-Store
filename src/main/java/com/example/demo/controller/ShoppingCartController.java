package com.example.demo.controller;

import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.CartItemUpdateDto;
import com.example.demo.dto.CreateCartItemRequestDto;
import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.model.User;
import com.example.demo.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management",
        description = "Endpoints for managing shopping cart")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart",
            description = "Retrieve current user's shopping cart")
    public ShoppingCartDto getShoppingCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.findByUserId(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to cart", description = "Add a book to the shopping cart")
    public ShoppingCartDto addBook(@AuthenticationPrincipal User user,
                                   @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.addBookToCart(user.getId(), requestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity", description = "Update quantity of a book in the cart")
    public CartItemDto updateQuantity(@AuthenticationPrincipal User user,
                                      @PathVariable Long cartItemId,
                                      @RequestBody @Valid CartItemUpdateDto updateDto) {
        return shoppingCartService.updateQuantity(user.getId(), cartItemId, updateDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove item", description = "Remove a book from the shopping cart")
    public void removeItem(@AuthenticationPrincipal User user,
                           @PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(user.getId(), cartItemId);
    }
}
