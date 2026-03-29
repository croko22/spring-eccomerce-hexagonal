package com.example.ecommerce.cart.domain.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long id) {
        super("Cart not found with id: " + id);
    }

    public CartNotFoundException(Long userId, String message) {
        super("Cart not found for user " + userId + ": " + message);
    }
}
