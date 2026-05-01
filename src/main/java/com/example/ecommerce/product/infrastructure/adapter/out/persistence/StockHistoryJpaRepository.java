package com.example.ecommerce.product.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockHistoryJpaRepository extends JpaRepository<StockHistoryEntity, Long> {

    List<StockHistoryEntity> findByProductIdOrderByCreatedAtDesc(Long productId);

    Page<StockHistoryEntity> findByProductId(Long productId, Pageable pageable);
}