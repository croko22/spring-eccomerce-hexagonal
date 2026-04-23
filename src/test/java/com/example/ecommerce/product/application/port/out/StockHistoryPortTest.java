package com.example.ecommerce.product.application.port.out;

import com.example.ecommerce.product.domain.model.StockChangeType;
import com.example.ecommerce.product.domain.model.StockHistory;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockHistoryPortTest {

    /**
     * Stub implementation to verify the interface contract is correctly defined.
     * If this compiles and runs, the port interface methods match the design.
     */
    private static class StubStockHistoryPort implements StockHistoryPort {
        private StockHistory lastRecorded;
        private final List<StockHistory> historyToReturn;

        StubStockHistoryPort(List<StockHistory> historyToReturn) {
            this.historyToReturn = historyToReturn;
        }

        @Override
        public void record(Long productId, StockChangeType type, int quantity, int stockAfter, String reference) {
            this.lastRecorded = new StockHistory(productId, type, quantity, stockAfter, reference);
        }

        @Override
        public Page<StockHistory> findByProductId(Long productId, Pageable pageable) {
            return new PageImpl<>(historyToReturn, pageable, historyToReturn.size());
        }
    }

    @Test
    void shouldRecordStockHistoryEntryViaPort() {
        StockHistoryPort port = new StubStockHistoryPort(List.of());

        port.record(1L, StockChangeType.RESERVE, 5, 10, "ORDER-123");

        // Verify method executed without error
        StockHistory recorded = ((StubStockHistoryPort) port).lastRecorded;
        assertNotNull(recorded);
        assertEquals(1L, recorded.getProductId());
        assertEquals(StockChangeType.RESERVE, recorded.getChangeType());
        assertEquals(5, recorded.getQuantity());
        assertEquals(10, recorded.getStockAfter());
        assertEquals("ORDER-123", recorded.getReference());
    }

    @Test
    void shouldFindByProductIdWithPagination() {
        StockHistory entry1 = new StockHistory(1L, StockChangeType.RESERVE, 5, 10, "ORD-1");
        StockHistory entry2 = new StockHistory(1L, StockChangeType.RELEASE, 3, 13, "ORD-2");
        StockHistoryPort port = new StubStockHistoryPort(List.of(entry1, entry2));

        Page<StockHistory> result = port.findByProductId(1L, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(StockChangeType.RESERVE, result.getContent().get(0).getChangeType());
        assertEquals(StockChangeType.RELEASE, result.getContent().get(1).getChangeType());
    }

    @Test
    void shouldReturnEmptyPageWhenNoHistoryExists() {
        StockHistoryPort port = new StubStockHistoryPort(List.of());

        Page<StockHistory> result = port.findByProductId(999L, PageRequest.of(0, 10));

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}
