package com.example.ecommerce.product.application.port.out;

import com.example.ecommerce.product.domain.model.StockChangeType;
import com.example.ecommerce.product.domain.model.StockHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockHistoryPort {

    void record(Long productId, StockChangeType type, int quantity, int stockAfter, String reference);

    Page<StockHistory> findByProductId(Long productId, Pageable pageable);
}
