package com.example.ecommerce.cart.application.port.in;

import com.example.ecommerce.cart.domain.model.Cart;

public interface GetCartUseCase {
    Cart getCartByUserId(Long userId);
}
