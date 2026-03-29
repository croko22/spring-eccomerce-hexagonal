package com.example.ecommerce.cart.infrastructure.config;

import com.example.ecommerce.cart.application.port.in.*;
import com.example.ecommerce.cart.application.port.out.CartRepositoryPort;
import com.example.ecommerce.cart.application.service.CartService;
import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartConfig {

    @Bean
    public CartService cartService(CartRepositoryPort cartRepositoryPort, 
                                    ProductRepositoryPort productRepositoryPort) {
        return new CartService(cartRepositoryPort, productRepositoryPort);
    }

    @Bean
    public AddToCartUseCase addToCartUseCase(CartService cartService) {
        return cartService;
    }

    @Bean
    public RemoveFromCartUseCase removeFromCartUseCase(CartService cartService) {
        return cartService;
    }

    @Bean
    public GetCartUseCase getCartUseCase(CartService cartService) {
        return cartService;
    }

    @Bean
    public ClearCartUseCase clearCartUseCase(CartService cartService) {
        return cartService;
    }

    @Bean
    public UpdateCartItemUseCase updateCartItemUseCase(CartService cartService) {
        return cartService;
    }
}
