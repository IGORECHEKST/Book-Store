package com.example.demo.mapper;

import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.model.ShoppingCart;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = CartItemMapper.class
)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
