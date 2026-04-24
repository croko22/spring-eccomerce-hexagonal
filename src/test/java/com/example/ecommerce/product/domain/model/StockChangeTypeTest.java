package com.example.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StockChangeTypeTest {

    @Test
    void shouldContainAllExpectedValues() {
        StockChangeType[] values = StockChangeType.values();

        assertEquals(4, values.length);
        assertNotNull(StockChangeType.valueOf("RESERVE"));
        assertNotNull(StockChangeType.valueOf("DECREMENT"));
        assertNotNull(StockChangeType.valueOf("RELEASE"));
        assertNotNull(StockChangeType.valueOf("ADJUST"));
    }

    @Test
    void shouldReturnCorrectNameForEnumConstants() {
        assertEquals("RESERVE", StockChangeType.RESERVE.name());
        assertEquals("DECREMENT", StockChangeType.DECREMENT.name());
        assertEquals("RELEASE", StockChangeType.RELEASE.name());
        assertEquals("ADJUST", StockChangeType.ADJUST.name());
    }
}
