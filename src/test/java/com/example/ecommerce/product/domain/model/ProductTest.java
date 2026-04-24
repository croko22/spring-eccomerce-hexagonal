package com.example.ecommerce.product.domain.model;

import com.example.ecommerce.product.domain.exception.InsufficientStockException;
import com.example.ecommerce.product.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    // ========== Existing tests (preserved) ==========

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

    // ========== Stock domain method tests (Phase 2) ==========

    private Product createProductWithStock(int stock) {
        return new Product(1L, "Test Product", "Description", 100.0, stock, null, "SKU-001", null);
    }

    @Nested
    class ReserveStock {

        @Test
        void shouldReserveStockSuccessfully() {
            // Arrange
            Product product = createProductWithStock(100);

            // Act
            product.reserveStock(30);

            // Assert
            assertEquals(70, product.getStock());
        }

        @Test
        void shouldThrowInsufficientStockExceptionWhenReservingMoreThanAvailable() {
            // Arrange
            Product product = createProductWithStock(10);

            // Act & Assert
            InsufficientStockException exception = assertThrows(InsufficientStockException.class,
                    () -> product.reserveStock(15));

            assertTrue(exception.getMessage().contains("Insufficient stock"));
        }

        @Test
        void shouldReserveEntireStock() {
            // Arrange
            Product product = createProductWithStock(50);

            // Act
            product.reserveStock(50);

            // Assert
            assertEquals(0, product.getStock());
        }
    }

    @Nested
    class ReleaseStock {

        @Test
        void shouldReleaseStockSuccessfully() {
            // Arrange
            Product product = createProductWithStock(50);

            // Act
            product.releaseStock(20);

            // Assert
            assertEquals(70, product.getStock());
        }

        @Test
        void shouldReleaseStockWithZeroQuantity() {
            // Arrange
            Product product = createProductWithStock(50);

            // Act
            product.releaseStock(0);

            // Assert
            assertEquals(50, product.getStock());
        }

        @Test
        void shouldReleaseLargeQuantity() {
            // Arrange
            Product product = createProductWithStock(10);

            // Act
            product.releaseStock(1000);

            // Assert
            assertEquals(1010, product.getStock());
        }
    }

    @Nested
    class DecrementStock {

        @Test
        void shouldDecrementStockSuccessfully() {
            // Arrange
            Product product = createProductWithStock(100);

            // Act
            product.decrementStock(30);

            // Assert
            assertEquals(70, product.getStock());
        }

        @Test
        void shouldThrowInsufficientStockExceptionWhenDecrementingMoreThanAvailable() {
            // Arrange
            Product product = createProductWithStock(10);

            // Act & Assert
            assertThrows(InsufficientStockException.class,
                    () -> product.decrementStock(15));
        }

        @Test
        void shouldDecrementToZero() {
            // Arrange
            Product product = createProductWithStock(25);

            // Act
            product.decrementStock(25);

            // Assert
            assertEquals(0, product.getStock());
        }
    }

    @Nested
    class AdjustStock {

        @Test
        void shouldAdjustStockWithPositiveQuantity() {
            // Arrange
            Product product = createProductWithStock(50);

            // Act
            product.adjustStock(10, "restock");

            // Assert
            assertEquals(60, product.getStock());
        }

        @Test
        void shouldAdjustStockWithNegativeQuantity() {
            // Arrange
            Product product = createProductWithStock(50);

            // Act
            product.adjustStock(-10, "correction");

            // Assert
            assertEquals(40, product.getStock());
        }

        @Test
        void shouldThrowInsufficientStockExceptionWhenAdjustWouldGoNegative() {
            // Arrange
            Product product = createProductWithStock(5);

            // Act & Assert
            assertThrows(InsufficientStockException.class,
                    () -> product.adjustStock(-10, "bad adjustment"));
        }

        @Test
        void shouldAdjustToExactZero() {
            // Arrange
            Product product = createProductWithStock(30);

            // Act
            product.adjustStock(-30, "clear stock");

            // Assert
            assertEquals(0, product.getStock());
        }
    }

    @Nested
    class LowStockThreshold {

        @Test
        void shouldReturnTrueWhenStockIsBelowThreshold() {
            // Arrange
            Product product = createProductWithStock(3);
            product.setLowStockThreshold(5);

            // Assert
            assertTrue(product.isLowStock());
        }

        @Test
        void shouldReturnFalseWhenStockIsAboveThreshold() {
            // Arrange
            Product product = createProductWithStock(10);
            product.setLowStockThreshold(5);

            // Assert
            assertFalse(product.isLowStock());
        }

        @Test
        void shouldReturnFalseWhenThresholdIsZero() {
            // Arrange — threshold 0 means disabled
            Product product = createProductWithStock(1);
            product.setLowStockThreshold(0);

            // Assert
            assertFalse(product.isLowStock());
        }

        @Test
        void shouldReturnTrueWhenStockEqualsThreshold() {
            // Arrange
            Product product = createProductWithStock(5);
            product.setLowStockThreshold(5);

            // Assert
            assertTrue(product.isLowStock());
        }

        @Test
        void shouldGetAndSetLowStockThreshold() {
            // Arrange
            Product product = createProductWithStock(10);

            // Act
            product.setLowStockThreshold(20);

            // Assert
            assertEquals(20, product.getLowStockThreshold());
        }

        @Test
        void shouldHaveDefaultThresholdOfZero() {
            // Arrange
            Product product = createProductWithStock(10);

            // Assert
            assertEquals(0, product.getLowStockThreshold());
        }
    }
}