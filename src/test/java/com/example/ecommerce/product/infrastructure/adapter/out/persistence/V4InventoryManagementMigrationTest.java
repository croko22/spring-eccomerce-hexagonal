package com.example.ecommerce.product.infrastructure.adapter.out.persistence;

import com.example.ecommerce.shared.infrastructure.PostgresContainerIntegrationTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Tag("integration")
class V4InventoryManagementMigrationTest extends PostgresContainerIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldAddLowStockThresholdColumnToProducts() {
        // Verify the column exists with correct type and default
        List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT column_name, data_type, column_default " +
                "FROM information_schema.columns " +
                "WHERE table_name = 'products' AND column_name = 'low_stock_threshold'"
        );

        assertFalse(columns.isEmpty(), "low_stock_threshold column should exist on products table");
        Map<String, Object> column = columns.get(0);
        assertEquals("integer", column.get("data_type"));
        assertEquals("0", column.get("column_default"));
    }

    @Test
    void shouldCreateStockHistoryTable() {
        // Verify the table exists
        Integer tableExists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'stock_history'",
                Integer.class
        );

        assertEquals(1, tableExists, "stock_history table should exist");
    }

    @Test
    void shouldHaveCorrectColumnsInStockHistoryTable() {
        // Verify key columns exist
        List<String> columnNames = jdbcTemplate.queryForList(
                "SELECT column_name FROM information_schema.columns WHERE table_name = 'stock_history' ORDER BY ordinal_position",
                String.class
        );

        assertTrue(columnNames.contains("id"), "stock_history should have 'id' column");
        assertTrue(columnNames.contains("product_id"), "stock_history should have 'product_id' column");
        assertTrue(columnNames.contains("change_type"), "stock_history should have 'change_type' column");
        assertTrue(columnNames.contains("quantity"), "stock_history should have 'quantity' column");
        assertTrue(columnNames.contains("stock_after"), "stock_history should have 'stock_after' column");
        assertTrue(columnNames.contains("reference"), "stock_history should have 'reference' column");
        assertTrue(columnNames.contains("created_at"), "stock_history should have 'created_at' column");
    }

    @Test
    void shouldHaveForeignKeyFromStockHistoryToProducts() {
        // Verify FK constraint exists
        Integer fkExists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.table_constraints " +
                "WHERE table_name = 'stock_history' AND constraint_type = 'FOREIGN KEY'",
                Integer.class
        );

        assertTrue(fkExists > 0, "stock_history should have a foreign key constraint");
    }

    @Test
    void shouldHaveIndexOnStockHistoryProductId() {
        // Verify index on product_id
        Integer indexExists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pg_indexes WHERE tablename = 'stock_history' AND indexname = 'idx_stock_history_product'",
                Integer.class
        );

        assertTrue(indexExists > 0, "stock_history should have idx_stock_history_product index");
    }
}
