package com.example.ecommerce.product.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientStockExceptionTest {

    @Test
    void shouldCreateExceptionWithProductIdAndRequestedAndAvailable() {
        InsufficientStockException ex = new InsufficientStockException(42L, 10, 3);

        assertTrue(ex.getMessage().contains("42"));
        assertTrue(ex.getMessage().contains("10"));
        assertTrue(ex.getMessage().contains("3"));
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        InsufficientStockException ex = new InsufficientStockException("Custom insufficient stock message");

        assertEquals("Custom insufficient stock message", ex.getMessage());
    }

    @Test
    void shouldBeRuntimeExceptionSubclass() {
        InsufficientStockException ex = new InsufficientStockException(1L, 5, 0);

        assertTrue(ex instanceof RuntimeException);
    }
}
