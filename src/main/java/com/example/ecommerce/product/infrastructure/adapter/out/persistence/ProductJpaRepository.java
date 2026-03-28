package com.example.ecommerce.product.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}