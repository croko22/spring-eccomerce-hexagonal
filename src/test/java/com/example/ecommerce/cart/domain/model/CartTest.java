package com.example.ecommerce.cart.domain.model;

import com.example.ecommerce.cart.domain.exception.InvalidCartException;
import com.example.ecommerce.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart(1L, 100L);
    }

    @Test
    void shouldCreateCartWithValidData() {
        assertEquals(1L, cart.getId());
        assertEquals(100L, cart.getUserId());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        assertThrows(InvalidCartException.class, () -> {
            new Cart(null, null);
        });
    }

    @Test
    void shouldAddItemToCart() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);

        cart.addItem(product, 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void shouldIncreaseQuantityWhenAddingExistingItem() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        cart.addItem(product, 2);
        cart.addItem(product, 3);

        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().get(0).getQuantity());
    }

    @Test
    void shouldRemoveItemFromCart() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        cart.addItem(product, 2);

        cart.removeItem(1L);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldUpdateItemQuantity() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        cart.addItem(product, 2);

        cart.updateItemQuantity(1L, 5);

        assertEquals(5, cart.getItems().get(0).getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentItem() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        cart.addItem(product, 2);

        assertThrows(InvalidCartException.class, () -> {
            cart.updateItemQuantity(99L, 5);
        });
    }

    @Test
    void shouldClearCart() {
        Product product1 = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        Product product2 = new Product(2L, "Mouse", "Wireless Mouse", 50.0);
        cart.addItem(product1, 1);
        cart.addItem(product2, 1);

        cart.clear();

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldCalculateTotal() {
        Product product1 = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        Product product2 = new Product(2L, "Mouse", "Wireless Mouse", 50.0);
        cart.addItem(product1, 2);
        cart.addItem(product2, 1);

        double total = cart.getTotal();

        assertEquals(3050.0, total);
    }

    @Test
    void shouldCalculateTotalItemCount() {
        Product product1 = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        Product product2 = new Product(2L, "Mouse", "Wireless Mouse", 50.0);
        cart.addItem(product1, 2);
        cart.addItem(product2, 3);

        int count = cart.getTotalItemCount();

        assertEquals(5, count);
    }

    @Test
    void shouldCreateCartWithExistingItems() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        CartItem item = new CartItem(1L, product, 2);
        List<CartItem> items = List.of(item);

        Cart cartWithItems = new Cart(1L, 100L, items);

        assertEquals(1, cartWithItems.getItems().size());
        assertEquals(2, cartWithItems.getItems().get(0).getQuantity());
    }
}
