package com.example.ecommerce.product.infrastructure.config;

import com.example.ecommerce.product.application.service.ProductService;
import com.example.ecommerce.product.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockAlertScheduler {

    private static final Logger log = LoggerFactory.getLogger(StockAlertScheduler.class);

    private final ProductService productService;

    public StockAlertScheduler(ProductService productService) {
        this.productService = productService;
    }

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void checkLowStockProducts() {
        List<Product> lowStockProducts = productService.getLowStockProducts();
        
        if (lowStockProducts.isEmpty()) {
            return;
        }
        
        log.warn("=== Low Stock Alert: {} product(s) below threshold ===", lowStockProducts.size());
        
        for (Product product : lowStockProducts) {
            log.warn("LOW STOCK: Product '{}' (ID: {}) has {} units (threshold: {})",
                    product.getName(),
                    product.getId(),
                    product.getStock(),
                    product.getLowStockThreshold());
        }
    }
}