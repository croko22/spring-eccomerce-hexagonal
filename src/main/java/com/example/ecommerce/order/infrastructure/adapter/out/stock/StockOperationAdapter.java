package com.example.ecommerce.order.infrastructure.adapter.out.stock;

import com.example.ecommerce.order.application.port.out.StockOperationPort;
import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.application.port.out.StockHistoryPort;
import com.example.ecommerce.product.domain.exception.InsufficientStockException;
import com.example.ecommerce.product.domain.exception.ProductNotFoundException;
import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.domain.model.StockChangeType;
import org.springframework.stereotype.Component;

@Component
public class StockOperationAdapter implements StockOperationPort {

    private final ProductRepositoryPort productRepositoryPort;
    private final StockHistoryPort stockHistoryPort;

    public StockOperationAdapter(ProductRepositoryPort productRepositoryPort, StockHistoryPort stockHistoryPort) {
        this.productRepositoryPort = productRepositoryPort;
        this.stockHistoryPort = stockHistoryPort;
    }

    @Override
    public void reserveStock(Long productId, int quantity, String reference) {
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        product.reserveStock(quantity);
        productRepositoryPort.save(product);
        stockHistoryPort.record(productId, StockChangeType.RESERVE, quantity, product.getStock(), reference);
    }

    @Override
    public void releaseStock(Long productId, int quantity, String reference) {
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        product.releaseStock(quantity);
        productRepositoryPort.save(product);
        stockHistoryPort.record(productId, StockChangeType.RELEASE, quantity, product.getStock(), reference);
    }

    @Override
    public void decrementStock(Long productId, int quantity, String reference) {
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        product.decrementStock(quantity);
        productRepositoryPort.save(product);
        stockHistoryPort.record(productId, StockChangeType.DECREMENT, quantity, product.getStock(), reference);
    }
}