package com.example.ecommerce.cart.application.service;

import com.example.ecommerce.cart.application.port.out.CartRepositoryPort;
import com.example.ecommerce.cart.domain.exception.CartNotFoundException;
import com.example.ecommerce.cart.domain.model.Cart;
import com.example.ecommerce.cart.domain.model.CartItem;
import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.domain.exception.ProductNotFoundException;
import com.example.ecommerce.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepositoryPort cartRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(cartRepositoryPort, productRepositoryPort);
    }

    @Test
    void shouldAddItemToNewCart() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        Cart emptyCart = new Cart(null, 100L);
        Cart savedCart = new Cart(1L, 100L);

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.empty());
        when(cartRepositoryPort.save(any(Cart.class))).thenReturn(savedCart);

        Cart result = cartService.addToCart(100L, 1L, 2);

        assertNotNull(result);
        verify(cartRepositoryPort).save(any(Cart.class));
    }

    @Test
    void shouldAddItemToExistingCart() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        Cart existingCart = new Cart(1L, 100L);
        Cart savedCart = new Cart(1L, 100L);

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.of(existingCart));
        when(cartRepositoryPort.save(any(Cart.class))).thenReturn(savedCart);

        Cart result = cartService.addToCart(100L, 1L, 2);

        assertNotNull(result);
        verify(cartRepositoryPort).save(any(Cart.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            cartService.addToCart(100L, 99L, 2);
        });
    }

    @Test
    void shouldRemoveItemFromCart() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        CartItem item = new CartItem(1L, product, 2);
        Cart cart = new Cart(1L, 100L, new ArrayList<>());
        cart.getItems().add(item);

        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.of(cart));
        when(cartRepositoryPort.save(any(Cart.class))).thenReturn(cart);

        cartService.removeFromCart(100L, 1L);

        verify(cartRepositoryPort).save(any(Cart.class));
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentItem() {
        Cart emptyCart = new Cart(1L, 100L);

        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.of(emptyCart));

        cartService.removeFromCart(100L, 99L);

        verify(cartRepositoryPort).save(any(Cart.class));
    }

    @Test
    void shouldGetCartByUserId() {
        Cart cart = new Cart(1L, 100L);
        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(100L);

        assertNotNull(result);
        assertEquals(100L, result.getUserId());
    }

    @Test
    void shouldReturnNewCartWhenCartNotFound() {
        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.empty());

        Cart result = cartService.getCartByUserId(100L);

        assertNotNull(result);
        assertEquals(100L, result.getUserId());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void shouldClearCart() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        CartItem item = new CartItem(1L, product, 2);
        Cart cart = new Cart(1L, 100L, new ArrayList<>());
        cart.getItems().add(item);

        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.of(cart));
        when(cartRepositoryPort.save(any(Cart.class))).thenReturn(cart);

        cartService.clearCart(100L);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepositoryPort).save(any(Cart.class));
    }

    @Test
    void shouldHandleClearingEmptyCart() {
        Cart emptyCart = new Cart(1L, 100L);

        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.of(emptyCart));
        when(cartRepositoryPort.save(any(Cart.class))).thenReturn(emptyCart);

        cartService.clearCart(100L);

        assertTrue(emptyCart.getItems().isEmpty());
        verify(cartRepositoryPort).save(any(Cart.class));
    }

    @Test
    void shouldUpdateCartItemQuantity() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);
        CartItem item = new CartItem(1L, product, 2);
        Cart cart = new Cart(1L, 100L, new ArrayList<>());
        cart.getItems().add(item);

        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.of(cart));
        when(cartRepositoryPort.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.updateCartItemQuantity(100L, 1L, 5);

        assertEquals(5, cart.getItems().get(0).getQuantity());
        verify(cartRepositoryPort).save(any(Cart.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentItem() {
        Cart emptyCart = new Cart(1L, 100L);

        when(cartRepositoryPort.findByUserId(100L)).thenReturn(Optional.of(emptyCart));

        assertThrows(com.example.ecommerce.cart.domain.exception.InvalidCartException.class, () -> {
            cartService.updateCartItemQuantity(100L, 1L, 5);
        });
    }
}
