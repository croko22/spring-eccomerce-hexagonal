package com.example.ecommerce.cart.infrastructure.adapter.in.web.dto;

import com.example.ecommerce.cart.domain.model.Cart;
import com.example.ecommerce.cart.domain.model.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {

    private Long id;
    private Long userId;
    private List<CartItemResponse> items;
    private double total;

    public static CartResponse fromDomain(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setItems(cart.getItems().stream()
                .map(CartItemResponse::fromDomain)
                .collect(Collectors.toList()));
        response.setTotal(cart.getTotal());
        return response;
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

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public static class CartItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private double productPrice;
        private int quantity;
        private double subtotal;

        public static CartItemResponse fromDomain(CartItem item) {
            CartItemResponse response = new CartItemResponse();
            response.setId(item.getId());
            response.setProductId(item.getProduct().getId());
            response.setProductName(item.getProduct().getName());
            response.setProductPrice(item.getProduct().getPrice());
            response.setQuantity(item.getQuantity());
            response.setSubtotal(item.getSubtotal());
            return response;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(double productPrice) {
            this.productPrice = productPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }
    }
}
