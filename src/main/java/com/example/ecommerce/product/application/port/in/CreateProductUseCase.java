package com.example.ecommerce.product.application.port.in;

import com.example.ecommerce.product.domain.model.Product;

public interface CreateProductUseCase {

    Product createProduct(Product product);
}