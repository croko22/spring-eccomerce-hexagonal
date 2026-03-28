package com.example.ecommerce.product.application.service;

import com.example.ecommerce.product.application.port.in.CreateProductUseCase;
import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.domain.model.Product;

public class ProductService implements CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepositoryPort.save(product);
    }
}