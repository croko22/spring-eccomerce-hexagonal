package com.example.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockHistoryTest {

    @Test
    void shouldCreateStockHistoryWithAllFields() {
        // Arrange
        Long productId = 42L;
        StockChangeType changeType = StockChangeType.RESERVE;
        int quantity = 5;
        int stockAfter = 10;
        String reference = "ORDER-123";

        // Act
        StockHistory history = new StockHistory(productId, changeType, quantity, stockAfter, reference);

        // Assert
        assertEquals(productId, history.getProductId());
        assertEquals(changeType, history.getChangeType());
        assertEquals(quantity, history.getQuantity());
        assertEquals(stockAfter, history.getStockAfter());
        assertEquals(reference, history.getReference());
        assertNotNull(history.getCreatedAt());
    }

    @Test
    void shouldCreateStockHistoryWithDifferentChangeType() {
        // Triangulation: different type and values
        StockHistory history = new StockHistory(99L, StockChangeType.RELEASE, 3, 20, "ORDER-456");

        assertEquals(99L, history.getProductId());
        assertEquals(StockChangeType.RELEASE, history.getChangeType());
        assertEquals(3, history.getQuantity());
        assertEquals(20, history.getStockAfter());
        assertEquals("ORDER-456", history.getReference());
    }

    @Test
    void shouldAllowNullReference() {
        StockHistory history = new StockHistory(1L, StockChangeType.ADJUST, 10, 100, null);

        assertNull(history.getReference());
        assertEquals(StockChangeType.ADJUST, history.getChangeType());
    }

    @Test
    void shouldSetCreatedAtToNonNullTimestamp() {
        StockHistory history = new StockHistory(1L, StockChangeType.DECREMENT, 1, 0, "REF");

        assertNotNull(history.getCreatedAt());
        // Verify it's a realistic timestamp (after year 2020)
        assertTrue(history.getCreatedAt().isAfter(java.time.LocalDateTime.of(2020, 1, 1, 0, 0)));
    }
}
