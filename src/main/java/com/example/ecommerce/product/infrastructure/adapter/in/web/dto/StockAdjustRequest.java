package com.example.ecommerce.product.infrastructure.adapter.in.web.dto;

public class StockAdjustRequest {

    private int quantity;
    private String reason;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}