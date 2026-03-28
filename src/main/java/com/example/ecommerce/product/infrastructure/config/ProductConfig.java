package com.example.ecommerce.product.infrastructure.config;

import com.example.ecommerce.product.application.port.in.CreateProductUseCase;
import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.application.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepositoryPort productRepositoryPort) {
        return new ProductService(productRepositoryPort);
    }
}