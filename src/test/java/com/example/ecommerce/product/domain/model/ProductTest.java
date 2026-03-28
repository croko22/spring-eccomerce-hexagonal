package com.example.ecommerce.product.domain.model;

import com.example.ecommerce.product.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void shouldCreateProductWithValidData() {
        // Arrange & Act
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 1500.0);

        // Assert
        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals("Gaming Laptop", product.getDescription());
        assertEquals(1500.0, product.getPrice());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Arrange
        // (No data setup needed, we create in the Act step)

        // Act & Assert
        Exception exception = assertThrows(InvalidProductException.class, () -> {
            new Product(null, "Laptop", "Gaming Laptop", -10.0);
        });

        assertEquals("Price cannot be negative", exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
         Exception exception = assertThrows(InvalidProductException.class, () -> {
            new Product(null, "", "Gaming Laptop", 10.0);
        });

        assertEquals("Product name cannot be blank", exception.getMessage());
    }
}