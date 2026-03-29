package com.example.ecommerce.cart.domain.model;

import com.example.ecommerce.cart.domain.exception.InvalidCartException;
import com.example.ecommerce.product.domain.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private Long id;
    private Long userId;
    private List<CartItem> items;

    public Cart(Long id, Long userId) {
        if (userId == null) {
            throw new InvalidCartException("User ID cannot be null");
        }
        this.id = id;
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public Cart(Long id, Long userId, List<CartItem> items) {
        if (userId == null) {
            throw new InvalidCartException("User ID cannot be null");
        }
        this.id = id;
        this.userId = userId;
        this.items = items != null ? items : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(Product product, int quantity) {
        CartItem existingItem = findItemByProductId(product.getId());
        
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.add(new CartItem(null, product, quantity));
        }
    }

    public void removeItem(Long productId) {
        CartItem item = findItemByProductId(productId);
        if (item != null) {
            items.remove(item);
        }
    }

    public void updateItemQuantity(Long productId, int quantity) {
        CartItem item = findItemByProductId(productId);
        if (item == null) {
            throw new InvalidCartException("Product not found in cart");
        }
        item.setQuantity(quantity);
    }

    public void clear() {
        items.clear();
    }

    private CartItem findItemByProductId(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public int getTotalItemCount() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
