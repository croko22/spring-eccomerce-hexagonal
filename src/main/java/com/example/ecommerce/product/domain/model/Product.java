package com.example.ecommerce.product.domain.model;

import com.example.ecommerce.product.domain.exception.InsufficientStockException;
import com.example.ecommerce.product.domain.exception.InvalidProductException;

public class Product {

    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String imageUrl;
    private String sku;
    private Long categoryId;
    private int lowStockThreshold;

    public Product(Long id, String name, String description, double price, int stock, String imageUrl, String sku, Long categoryId) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be blank");
        }
        if (price < 0) {
            throw new InvalidProductException("Price cannot be negative");
        }
        if (stock < 0) {
            throw new InvalidProductException("Stock cannot be negative");
        }
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.sku = sku;
        this.categoryId = categoryId;
        this.lowStockThreshold = 0;
    }

    // Legacy constructor for backward compatibility
    public Product(Long id, String name, String description, double price) {
        this(id, name, description, price, 0, null, null, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new InvalidProductException("Price cannot be negative");
        }
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new InvalidProductException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    // ========== Stock domain methods ==========

    /**
     * Reserves stock for an order by decrementing available quantity.
     * @throws InsufficientStockException if requested quantity exceeds available stock
     */
    public void reserveStock(int quantity) {
        if (quantity > stock) {
            throw new InsufficientStockException(id, quantity, stock);
        }
        stock -= quantity;
    }

    /**
     * Releases previously reserved stock back to available quantity.
     */
    public void releaseStock(int quantity) {
        stock += quantity;
    }

    /**
     * Permanently decrements stock (e.g., on payment confirmation).
     * @throws InsufficientStockException if requested quantity exceeds available stock
     */
    public void decrementStock(int quantity) {
        if (quantity > stock) {
            throw new InsufficientStockException(id, quantity, stock);
        }
        stock -= quantity;
    }

    /**
     * Adjusts stock by a delta amount (positive or negative).
     * Used for admin corrections, restocking, etc.
     * @param quantity the adjustment delta (positive increases, negative decreases)
     * @param reason the reason for the adjustment
     * @throws InsufficientStockException if resulting stock would be negative
     */
    public void adjustStock(int quantity, String reason) {
        int newStock = stock + quantity;
        if (newStock < 0) {
            throw new InsufficientStockException(id, Math.abs(quantity), stock);
        }
        stock = newStock;
    }

    /**
     * Checks if the product is flagged as low stock.
     * Low stock is true when: stock <= lowStockThreshold AND lowStockThreshold > 0.
     * A threshold of 0 means the check is disabled.
     */
    public boolean isLowStock() {
        return lowStockThreshold > 0 && stock <= lowStockThreshold;
    }
}