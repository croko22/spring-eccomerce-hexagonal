package com.example.ecommerce.cart.application.port.in;

import com.example.ecommerce.cart.domain.model.Cart;

public interface UpdateCartItemUseCase {
    Cart updateCartItemQuantity(Long userId, Long productId, int quantity);
}
