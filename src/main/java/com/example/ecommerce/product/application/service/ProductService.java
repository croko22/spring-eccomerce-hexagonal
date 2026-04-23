package com.example.ecommerce.product.application.service;

import com.example.ecommerce.product.application.port.in.CreateProductUseCase;
import com.example.ecommerce.product.application.port.in.DeleteProductUseCase;
import com.example.ecommerce.product.application.port.in.GetProductUseCase;
import com.example.ecommerce.product.application.port.in.UpdateProductUseCase;
import com.example.ecommerce.product.application.port.out.CategoryRepositoryPort;
import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.application.port.out.StockHistoryPort;
import com.example.ecommerce.product.domain.exception.CategoryNotFoundException;
import com.example.ecommerce.product.domain.exception.ProductNotFoundException;
import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.domain.model.StockChangeType;
import com.example.ecommerce.product.domain.model.StockHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ProductService implements CreateProductUseCase, GetProductUseCase, UpdateProductUseCase, DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;
    private final StockHistoryPort stockHistoryPort;

    public ProductService(ProductRepositoryPort productRepositoryPort, CategoryRepositoryPort categoryRepositoryPort) {
        this(productRepositoryPort, categoryRepositoryPort, null);
    }

    public ProductService(ProductRepositoryPort productRepositoryPort, CategoryRepositoryPort categoryRepositoryPort, StockHistoryPort stockHistoryPort) {
        this.productRepositoryPort = productRepositoryPort;
        this.categoryRepositoryPort = categoryRepositoryPort;
        this.stockHistoryPort = stockHistoryPort;
    }

    @Override
    public Product createProduct(Product product) {
        validateCategoryExists(product.getCategoryId());
        return productRepositoryPort.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepositoryPort.findAll();
    }

    @Override
    public Product updateProduct(Long id, Product productToUpdate) {
        Product existingProduct = getProductById(id);
        validateCategoryExists(productToUpdate.getCategoryId());
        
        existingProduct.setName(productToUpdate.getName());
        existingProduct.setDescription(productToUpdate.getDescription());
        existingProduct.setPrice(productToUpdate.getPrice());
        existingProduct.setStock(productToUpdate.getStock());
        existingProduct.setImageUrl(productToUpdate.getImageUrl());
        existingProduct.setSku(productToUpdate.getSku());
        existingProduct.setCategoryId(productToUpdate.getCategoryId());
        
        return productRepositoryPort.save(existingProduct);
    }

    private void validateCategoryExists(Long categoryId) {
        if (categoryId == null) {
            return;
        }

        categoryRepositoryPort.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Override
    public void deleteProduct(Long id) {
        getProductById(id); // Check if exists
        productRepositoryPort.deleteById(id);
    }

    public Page<Product> searchProducts(Long categoryId, Double minPrice, Double maxPrice,
                                         Boolean inStock, String search, Pageable pageable) {
        return productRepositoryPort.findByFilters(categoryId, minPrice, maxPrice, inStock, search, pageable);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepositoryPort.findByCategoryId(categoryId);
    }

    public void reserveStock(Long productId, int quantity, String reference) {
        Product product = getProductById(productId);
        product.reserveStock(quantity);
        productRepositoryPort.save(product);
        if (stockHistoryPort != null) {
            stockHistoryPort.record(productId, StockChangeType.RESERVE, quantity, product.getStock(), reference);
        }
    }

    public void releaseStock(Long productId, int quantity, String reference) {
        Product product = getProductById(productId);
        product.releaseStock(quantity);
        productRepositoryPort.save(product);
        if (stockHistoryPort != null) {
            stockHistoryPort.record(productId, StockChangeType.RELEASE, quantity, product.getStock(), reference);
        }
    }

    public void decrementStock(Long productId, int quantity, String reference) {
        Product product = getProductById(productId);
        product.decrementStock(quantity);
        productRepositoryPort.save(product);
        if (stockHistoryPort != null) {
            stockHistoryPort.record(productId, StockChangeType.DECREMENT, quantity, product.getStock(), reference);
        }
    }

    public void adjustStock(Long productId, int quantity, String reference) {
        Product product = getProductById(productId);
        product.adjustStock(quantity, reference);
        productRepositoryPort.save(product);
        if (stockHistoryPort != null) {
            stockHistoryPort.record(productId, StockChangeType.ADJUST, quantity, product.getStock(), reference);
        }
    }

    public List<Product> getLowStockProducts() {
        return productRepositoryPort.findLowStockProducts();
    }

    public Page<StockHistory> getStockHistory(Long productId, Pageable pageable) {
        return stockHistoryPort.findByProductId(productId, pageable);
    }
}
