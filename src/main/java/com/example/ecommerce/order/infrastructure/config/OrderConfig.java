package com.example.ecommerce.order.infrastructure.config;

import com.example.ecommerce.order.application.port.out.CartPort;
import com.example.ecommerce.order.application.port.out.OrderRepositoryPort;
import com.example.ecommerce.order.application.port.out.StockOperationPort;
import com.example.ecommerce.order.application.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public OrderService orderService(OrderRepositoryPort orderRepositoryPort, CartPort cartPort, StockOperationPort stockOperationPort) {
        return new OrderService(orderRepositoryPort, cartPort, stockOperationPort);
    }
}
