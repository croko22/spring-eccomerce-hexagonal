package com.example.ecommerce.cart.application.port.in;

public interface RemoveFromCartUseCase {
    void removeFromCart(Long userId, Long productId);
}
