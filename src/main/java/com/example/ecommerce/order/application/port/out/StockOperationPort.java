package com.example.ecommerce.order.application.port.out;

public interface StockOperationPort {

    void reserveStock(Long productId, int quantity, String reference);

    void releaseStock(Long productId, int quantity, String reference);

    void decrementStock(Long productId, int quantity, String reference);
}