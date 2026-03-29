package com.example.ecommerce.cart.application.port.in;

import com.example.ecommerce.cart.domain.model.Cart;

public interface AddToCartUseCase {
    Cart addToCart(Long userId, Long productId, int quantity);
}
