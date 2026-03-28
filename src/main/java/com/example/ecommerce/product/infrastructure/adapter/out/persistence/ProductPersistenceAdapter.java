package com.example.ecommerce.product.infrastructure.adapter.out.persistence;

import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;

    public ProductPersistenceAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product save(Product product) {
        // Map Domain Model to Entity
        ProductEntity entity = new ProductEntity();
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        
        // Save using Spring Data JPA
        ProductEntity savedEntity = productJpaRepository.save(entity);
        
        // Map back to Domain Model
        return new Product(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getDescription(),
                savedEntity.getPrice()
        );
    }
}