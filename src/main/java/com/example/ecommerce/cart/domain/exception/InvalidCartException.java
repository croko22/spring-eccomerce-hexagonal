package com.example.ecommerce.cart.domain.exception;

public class InvalidCartException extends RuntimeException {
    public InvalidCartException(String message) {
        super(message);
    }
}
