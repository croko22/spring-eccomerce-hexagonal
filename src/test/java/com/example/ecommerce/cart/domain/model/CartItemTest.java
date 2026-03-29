package com.example.ecommerce.cart.domain.model;

import com.example.ecommerce.cart.domain.exception.InvalidCartException;
import com.example.ecommerce.product.domain.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void shouldCreateCartItemWithValidData() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        CartItem item = new CartItem(1L, product, 2);

        assertEquals(1L, item.getId());
        assertEquals(product, item.getProduct());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenProductIsNull() {
        assertThrows(InvalidCartException.class, () -> {
            new CartItem(null, null, 2);
        });
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);

        assertThrows(InvalidCartException.class, () -> {
            new CartItem(null, product, 0);
        });
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);

        assertThrows(InvalidCartException.class, () -> {
            new CartItem(null, product, -1);
        });
    }

    @Test
    void shouldCalculateSubtotal() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        CartItem item = new CartItem(null, product, 2);

        double subtotal = item.getSubtotal();

        assertEquals(3000.0, subtotal);
    }

    @Test
    void shouldUpdateQuantity() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        CartItem item = new CartItem(null, product, 2);

        item.setQuantity(5);

        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenSettingQuantityToZero() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        CartItem item = new CartItem(null, product, 2);

        assertThrows(InvalidCartException.class, () -> {
            item.setQuantity(0);
        });
    }
}
