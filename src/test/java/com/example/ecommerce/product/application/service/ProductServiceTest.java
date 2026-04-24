package com.example.ecommerce.product.application.service;

import com.example.ecommerce.product.application.port.out.CategoryRepositoryPort;
import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.application.port.out.StockHistoryPort;
import com.example.ecommerce.product.domain.exception.InsufficientStockException;
import com.example.ecommerce.product.domain.exception.ProductNotFoundException;
import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.domain.model.StockChangeType;
import com.example.ecommerce.product.domain.model.StockHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    @Mock
    private StockHistoryPort stockHistoryPort;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepositoryPort, categoryRepositoryPort, stockHistoryPort);
    }

    // ========== Existing tests (preserved) ==========

    @Test
    void shouldCreateProductSuccessfully() {
        Product productToCreate = new Product(null, "Test Product", "A great product", 99.99);
        Product savedProduct = new Product(1L, "Test Product", "A great product", 99.99);

        when(productRepositoryPort.save(any(Product.class))).thenReturn(savedProduct);

        Product createdProduct = productService.createProduct(productToCreate);

        assertNotNull(createdProduct);
        assertEquals(1L, createdProduct.getId());
        assertEquals("Test Product", createdProduct.getName());

        verify(productRepositoryPort).save(any(Product.class));
    }

    @Test
    void shouldReturnProductWhenIdExists() {
        Product existingProduct = new Product(1L, "Test Product", "A great product", 99.99);
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(existingProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(productRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void shouldReturnAllProducts() {
        List<Product> products = List.of(
                new Product(1L, "Product 1", "Description 1", 10.0),
                new Product(2L, "Product 2", "Description 2", 20.0)
        );
        when(productRepositoryPort.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        Product existingProduct = new Product(1L, "Old Name", "Old Desc", 10.0);
        Product updateRequest = new Product(null, "New Name", "New Desc", 20.0);
        Product updatedProduct = new Product(1L, "New Name", "New Desc", 20.0);

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepositoryPort.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updateRequest);

        assertEquals("New Name", result.getName());
        assertEquals(20.0, result.getPrice());
        verify(productRepositoryPort).save(any(Product.class));
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        Product existingProduct = new Product(1L, "Test Product", "A great product", 99.99);
        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(1L);

        verify(productRepositoryPort).findById(1L);
        verify(productRepositoryPort).deleteById(1L);
    }

    // ========== Stock use-case tests (Phase 2) ==========

    private Product createProductWithStock(Long id, int stock) {
        return new Product(id, "Test Product", "Description", 100.0, stock, null, "SKU-001", null);
    }

    @Nested
    class ReserveStockUseCase {

        @Test
        void shouldReserveStockAndRecordHistory() {
            // Arrange
            Product product = createProductWithStock(1L, 50);
            when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));
            when(productRepositoryPort.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            productService.reserveStock(1L, 10, "ORDER-123");

            // Assert
            assertEquals(40, product.getStock());
            verify(productRepositoryPort).save(product);
            verify(stockHistoryPort).record(1L, StockChangeType.RESERVE, 10, 40, "ORDER-123");
        }

        @Test
        void shouldThrowWhenReservingStockForNonExistentProduct() {
            // Arrange
            when(productRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ProductNotFoundException.class,
                    () -> productService.reserveStock(99L, 5, "ORDER-X"));
            verify(stockHistoryPort, never()).record(any(), any(), anyInt(), anyInt(), any());
        }

        @Test
        void shouldThrowAndNotRecordHistoryWhenInsufficientStock() {
            // Arrange
            Product product = createProductWithStock(1L, 5);
            when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));

            // Act & Assert
            assertThrows(InsufficientStockException.class,
                    () -> productService.reserveStock(1L, 10, "ORDER-456"));
            verify(stockHistoryPort, never()).record(any(), any(), anyInt(), anyInt(), any());
            verify(productRepositoryPort, never()).save(any());
        }
    }

    @Nested
    class ReleaseStockUseCase {

        @Test
        void shouldReleaseStockAndRecordHistory() {
            // Arrange
            Product product = createProductWithStock(1L, 30);
            when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));
            when(productRepositoryPort.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            productService.releaseStock(1L, 20, "CANCEL-100");

            // Assert
            assertEquals(50, product.getStock());
            verify(productRepositoryPort).save(product);
            verify(stockHistoryPort).record(1L, StockChangeType.RELEASE, 20, 50, "CANCEL-100");
        }

        @Test
        void shouldThrowWhenReleasingStockForNonExistentProduct() {
            // Arrange
            when(productRepositoryPort.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ProductNotFoundException.class,
                    () -> productService.releaseStock(99L, 5, "CANCEL-X"));
            verify(stockHistoryPort, never()).record(any(), any(), anyInt(), anyInt(), any());
        }
    }

    @Nested
    class DecrementStockUseCase {

        @Test
        void shouldDecrementStockAndRecordHistory() {
            // Arrange
            Product product = createProductWithStock(1L, 40);
            when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));
            when(productRepositoryPort.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            productService.decrementStock(1L, 10, "PAYMENT-777");

            // Assert
            assertEquals(30, product.getStock());
            verify(productRepositoryPort).save(product);
            verify(stockHistoryPort).record(1L, StockChangeType.DECREMENT, 10, 30, "PAYMENT-777");
        }

        @Test
        void shouldThrowAndNotRecordWhenInsufficientStockForDecrement() {
            // Arrange
            Product product = createProductWithStock(1L, 3);
            when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));

            // Act & Assert
            assertThrows(InsufficientStockException.class,
                    () -> productService.decrementStock(1L, 5, "PAYMENT-X"));
            verify(stockHistoryPort, never()).record(any(), any(), anyInt(), anyInt(), any());
        }
    }

    @Nested
    class AdjustStockUseCase {

        @Test
        void shouldAdjustStockPositiveAndRecordHistory() {
            // Arrange
            Product product = createProductWithStock(1L, 50);
            when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));
            when(productRepositoryPort.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            productService.adjustStock(1L, 25, "restock from supplier");

            // Assert
            assertEquals(75, product.getStock());
            verify(productRepositoryPort).save(product);
            verify(stockHistoryPort).record(1L, StockChangeType.ADJUST, 25, 75, "restock from supplier");
        }

        @Test
        void shouldAdjustStockNegativeAndRecordHistory() {
            // Arrange
            Product product = createProductWithStock(1L, 50);
            when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));
            when(productRepositoryPort.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            productService.adjustStock(1L, -10, "inventory correction");

            // Assert
            assertEquals(40, product.getStock());
            verify(productRepositoryPort).save(product);
            verify(stockHistoryPort).record(1L, StockChangeType.ADJUST, -10, 40, "inventory correction");
        }

        @Test
        void shouldThrowWhenAdjustWouldGoNegative() {
            // Arrange
            Product product = createProductWithStock(1L, 5);
            when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(product));

            // Act & Assert
            assertThrows(InsufficientStockException.class,
                    () -> productService.adjustStock(1L, -10, "bad adjustment"));
            verify(stockHistoryPort, never()).record(any(), any(), anyInt(), anyInt(), any());
            verify(productRepositoryPort, never()).save(any());
        }
    }

    @Nested
    class GetLowStockProductsUseCase {

        @Test
        void shouldReturnLowStockProducts() {
            // Arrange
            Product p1 = createProductWithStock(1L, 2);
            p1.setLowStockThreshold(5);
            Product p2 = createProductWithStock(2L, 1);
            p2.setLowStockThreshold(3);
            when(productRepositoryPort.findLowStockProducts()).thenReturn(List.of(p1, p2));

            // Act
            List<Product> result = productService.getLowStockProducts();

            // Assert
            assertEquals(2, result.size());
            assertEquals(1L, result.get(0).getId());
            assertEquals(2, result.get(0).getStock());
        }

        @Test
        void shouldReturnEmptyListWhenNoLowStockProducts() {
            // Arrange
            when(productRepositoryPort.findLowStockProducts()).thenReturn(List.of());

            // Act
            List<Product> result = productService.getLowStockProducts();

            // Assert
            assertNotNull(result);
            assertEquals(0, result.size());
        }
    }

    @Nested
    class GetStockHistoryUseCase {

        @Test
        void shouldReturnStockHistoryForProduct() {
            // Arrange
            Long productId = 1L;
            Pageable pageable = PageRequest.of(0, 10);
            List<StockHistory> historyEntries = List.of(
                    new StockHistory(productId, StockChangeType.ADJUST, 10, 60, "restock"),
                    new StockHistory(productId, StockChangeType.RESERVE, 5, 50, "ORDER-1")
            );
            Page<StockHistory> historyPage = new PageImpl<>(historyEntries, pageable, 2);
            when(stockHistoryPort.findByProductId(productId, pageable)).thenReturn(historyPage);

            // Act
            Page<StockHistory> result = productService.getStockHistory(productId, pageable);

            // Assert
            assertEquals(2, result.getContent().size());
            assertEquals(StockChangeType.ADJUST, result.getContent().get(0).getChangeType());
            assertEquals(StockChangeType.RESERVE, result.getContent().get(1).getChangeType());
            verify(stockHistoryPort).findByProductId(productId, pageable);
        }
    }
}
