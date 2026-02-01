package com.example.demo.mapper;

import com.example.demo.dto.OrderItemResponseDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.OrderItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "cartItem.book.price")
    @Mapping(target = "order", ignore = true)
    OrderItem toModel(CartItem cartItem);
}
