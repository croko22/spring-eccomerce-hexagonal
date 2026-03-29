package com.example.ecommerce.cart.application.port.out;

import com.example.ecommerce.cart.domain.model.Cart;

import java.util.Optional;

public interface CartRepositoryPort {
    Cart save(Cart cart);
    Optional<Cart> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
