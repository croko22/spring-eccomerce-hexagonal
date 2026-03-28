package com.example.ecommerce.product.domain.model;

import com.example.ecommerce.product.domain.exception.InvalidProductException;

public class Product {

    private Long id;
    private String name;
    private String description;
    private double price;

    public Product(Long id, String name, String description, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be blank");
        }
        if (price < 0) {
            throw new InvalidProductException("Price cannot be negative");
        }
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
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
}