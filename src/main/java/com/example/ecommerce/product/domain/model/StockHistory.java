package com.example.ecommerce.product.domain.model;

import java.time.LocalDateTime;

public class StockHistory {

    private final Long productId;
    private final StockChangeType changeType;
    private final int quantity;
    private final int stockAfter;
    private final String reference;
    private final LocalDateTime createdAt;

    public StockHistory(Long productId, StockChangeType changeType, int quantity, int stockAfter, String reference) {
        this.productId = productId;
        this.changeType = changeType;
        this.quantity = quantity;
        this.stockAfter = stockAfter;
        this.reference = reference;
        this.createdAt = LocalDateTime.now();
    }

    public Long getProductId() {
        return productId;
    }

    public StockChangeType getChangeType() {
        return changeType;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStockAfter() {
        return stockAfter;
    }

    public String getReference() {
        return reference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
