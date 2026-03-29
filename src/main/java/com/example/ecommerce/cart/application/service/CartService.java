package com.example.ecommerce.cart.application.service;

import com.example.ecommerce.cart.application.port.in.*;
import com.example.ecommerce.cart.application.port.out.CartRepositoryPort;
import com.example.ecommerce.cart.domain.exception.CartNotFoundException;
import com.example.ecommerce.cart.domain.model.Cart;
import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.domain.exception.ProductNotFoundException;
import com.example.ecommerce.product.domain.model.Product;

public class CartService implements AddToCartUseCase, RemoveFromCartUseCase, GetCartUseCase, ClearCartUseCase, UpdateCartItemUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public CartService(CartRepositoryPort cartRepositoryPort, ProductRepositoryPort productRepositoryPort) {
        this.cartRepositoryPort = cartRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Cart addToCart(Long userId, Long productId, int quantity) {
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Cart cart = cartRepositoryPort.findByUserId(userId)
                .orElse(new Cart(null, userId));

        cart.addItem(product, quantity);

        return cartRepositoryPort.save(cart);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId, "Cart is empty"));

        cart.removeItem(productId);

        cartRepositoryPort.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepositoryPort.findByUserId(userId)
                .orElse(new Cart(null, userId));
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId, "Cart is already empty"));

        cart.clear();

        cartRepositoryPort.save(cart);
    }

    @Override
    public Cart updateCartItemQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId, "Cart is empty"));

        cart.updateItemQuantity(productId, quantity);

        return cartRepositoryPort.save(cart);
    }
}
