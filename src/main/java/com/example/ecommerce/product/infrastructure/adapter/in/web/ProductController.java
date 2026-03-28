package com.example.ecommerce.product.infrastructure.adapter.in.web;

import com.example.ecommerce.product.application.port.in.CreateProductUseCase;
import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.infrastructure.adapter.in.web.dto.ProductRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;

    public ProductController(CreateProductUseCase createProductUseCase) {
        this.createProductUseCase = createProductUseCase;
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductRequest productRequest) {
        Product product = new Product(null, productRequest.getName(), productRequest.getDescription(), productRequest.getPrice());
        return createProductUseCase.createProduct(product);
    }
}