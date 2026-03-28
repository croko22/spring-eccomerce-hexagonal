package com.example.ecommerce.product.application.port.out;

import com.example.ecommerce.product.domain.model.Product;

public interface ProductRepositoryPort {

    Product save(Product product);
}