package com.ecomerce.order.service;

import com.ecomerce.order.dto.OrderItemDto;
import com.ecomerce.order.dto.OrderResponse;
import com.ecomerce.order.model.*;
import com.ecomerce.order.repository.OrderRepository;
//import com.ecomerce.order.repository.UserRepository;
import com.ecomerce.order.model.CartItem;
import com.ecomerce.order.model.Order;
import com.ecomerce.order.model.OrderItem;
import com.ecomerce.order.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private CartService cartService;
//
//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userId) {
        // Validate for cart items
        List<CartItem> cartItems = cartService.getUserCartItems(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }
//
//        // Validate for a user
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty()) {
//            return Optional.empty();
//        }
//
//        User user = userOptional.get();
        // Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                        null,
                        cartItem.getProductId(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order
                )).toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(String.valueOf(savedOrder.getId()));
        orderResponse.setTotalAmount(savedOrder.getTotalAmount());
        List<OrderItemDto> orderItemDtos = savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDto(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
//                                orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                                orderItem.getPrice()
                        )).toList();
        orderResponse.setItems(orderItemDtos);
        orderResponse.setStatus(savedOrder.getStatus());
        orderResponse.setCreatedAt(savedOrder.getCreatedAt());
        return orderResponse;

    }
}
