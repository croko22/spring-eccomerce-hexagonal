package com.example.ecommerce.cart.domain.model;

import com.example.ecommerce.cart.domain.exception.InvalidCartException;
import com.example.ecommerce.product.domain.model.Product;

public class CartItem {

    private Long id;
    private Product product;
    private int quantity;

    public CartItem(Long id, Product product, int quantity) {
        if (product == null) {
            throw new InvalidCartException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new InvalidCartException("Quantity must be greater than 0");
        }
        
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidCartException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}
