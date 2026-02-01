package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequestDto;
import com.example.demo.dto.OrderItemResponseDto;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.dto.UpdateOrderStatusRequestDto;
import com.example.demo.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(User user, CreateOrderRequestDto requestDto);

    List<OrderResponseDto> getAll(User user, Pageable pageable);

    OrderResponseDto updateStatus(Long id, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemResponseDto> getOrderItems(Long orderId);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}
