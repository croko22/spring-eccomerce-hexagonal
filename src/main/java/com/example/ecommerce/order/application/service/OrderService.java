package com.example.ecommerce.order.application.service;

import com.example.ecommerce.cart.domain.model.Cart;
import com.example.ecommerce.cart.domain.model.CartItem;
import com.example.ecommerce.order.application.port.in.*;
import com.example.ecommerce.order.application.port.out.CartPort;
import com.example.ecommerce.order.application.port.out.OrderRepositoryPort;
import com.example.ecommerce.order.application.port.out.StockOperationPort;
import com.example.ecommerce.order.domain.exception.DirectOrderPaidTransitionNotAllowedException;
import com.example.ecommerce.order.domain.exception.OrderNotFoundException;
import com.example.ecommerce.order.domain.model.Order;
import com.example.ecommerce.order.domain.model.OrderItem;
import com.example.ecommerce.order.domain.model.OrderStatus;
import com.example.ecommerce.order.domain.model.ShippingAddress;

import java.util.List;

public class OrderService implements CreateOrderUseCase, GetOrderUseCase, GetUserOrdersUseCase, UpdateOrderStatusUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final CartPort cartPort;
    private final StockOperationPort stockOperationPort;

    public OrderService(OrderRepositoryPort orderRepositoryPort, CartPort cartPort, StockOperationPort stockOperationPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.cartPort = cartPort;
        this.stockOperationPort = stockOperationPort;
    }

    @Override
    public Order createOrder(Long userId, CreateOrderCommand command) {
        Cart cart = cartPort.getCartByUserId(userId);
        
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot create order from empty cart");
        }

        // Reserve stock for all items first (fail fast if insufficient)
        for (CartItem item : cart.getItems()) {
            stockOperationPort.reserveStock(
                    item.getProduct().getId(),
                    item.getQuantity(),
                    "ORDER-RESERVE"
            );
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(this::mapToOrderItem)
                .toList();

        ShippingAddress shippingAddress = new ShippingAddress(
                command.street(),
                command.city(),
                command.state(),
                command.zipCode(),
                command.country()
        );

        Order order = new Order(
                null,
                null,
                userId,
                orderItems,
                0,
                OrderStatus.PENDING,
                shippingAddress,
                null,
                null
        );

        Order savedOrder = orderRepositoryPort.save(order);
        cartPort.clearCart(userId);
        
        return savedOrder;
    }

    private OrderItem mapToOrderItem(CartItem cartItem) {
        return new OrderItem(
                null,
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice()
        );
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public Order getOrderByNumber(String orderNumber) {
        return orderRepositoryPort.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderNumber));
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepositoryPort.findByUserId(userId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);

        // Handle stock operations on status transitions
        if (newStatus == OrderStatus.CANCELLED) {
            // Release reserved stock on cancellation
            for (OrderItem item : order.getItems()) {
                stockOperationPort.releaseStock(
                        item.getProductId(),
                        item.getQuantity(),
                        "ORDER-CANCEL-" + orderId
                );
            }
        } else if (newStatus == OrderStatus.PAID) {
            // Decrement stock on payment
            for (OrderItem item : order.getItems()) {
                stockOperationPort.decrementStock(
                        item.getProductId(),
                        item.getQuantity(),
                        "ORDER-PAID-" + orderId
                );
            }
        } else {
            // Only PAID and CANCELLED trigger stock operations
            // Other transitions handled by Order.updateStatus()
        }

        order.updateStatus(newStatus);
        return orderRepositoryPort.save(order);
    }
}
