package com.example.ecommerce.product.infrastructure.adapter.out.persistence;

import com.example.ecommerce.product.application.port.out.StockHistoryPort;
import com.example.ecommerce.product.domain.model.StockChangeType;
import com.example.ecommerce.product.domain.model.StockHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockHistoryPersistenceAdapter implements StockHistoryPort {

    private final StockHistoryJpaRepository stockHistoryJpaRepository;

    public StockHistoryPersistenceAdapter(StockHistoryJpaRepository stockHistoryJpaRepository) {
        this.stockHistoryJpaRepository = stockHistoryJpaRepository;
    }

    @Override
    public void record(Long productId, StockChangeType type, int quantity, int stockAfter, String reference) {
        StockHistoryEntity entity = new StockHistoryEntity();
        entity.setProductId(productId);
        entity.setChangeType(type.name());
        entity.setQuantity(quantity);
        entity.setStockAfter(stockAfter);
        entity.setReference(reference);
        entity.setCreatedAt(java.time.LocalDateTime.now());
        stockHistoryJpaRepository.save(entity);
    }

    @Override
    public Page<StockHistory> findByProductId(Long productId, Pageable pageable) {
        return stockHistoryJpaRepository.findByProductId(productId, pageable)
                .map(this::mapToDomainModel);
    }

    private StockHistory mapToDomainModel(StockHistoryEntity entity) {
        return new StockHistory(
                entity.getProductId(),
                StockChangeType.valueOf(entity.getChangeType()),
                entity.getQuantity(),
                entity.getStockAfter(),
                entity.getReference()
        );
    }
}