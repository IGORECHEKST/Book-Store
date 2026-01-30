package com.example.demo.service.impl;

import com.example.demo.dto.CreateOrderRequestDto;
import com.example.demo.dto.OrderItemResponseDto;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.dto.UpdateOrderStatusRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(User user, CreateOrderRequestDto requestDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping cart for user "
                        + user.getId()));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Can't place an order with an empty cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());

        BigDecimal total = cart.getCartItems().stream()
                .map(item -> item.getBook()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> convertToOrderItem(cartItem, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        shoppingCartService.clear(cart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getAll(User user, Pageable pageable) {
        return orderRepository.findAllByUserId(user.getId(), pageable).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto updateStatus(Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id " + id));
        order.setStatus(Status.valueOf(requestDto.getStatus()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId).stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long itemId) {
        return orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item " + itemId
                        + " for order " + orderId));
    }

    private OrderItem convertToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItem;
    }
}
