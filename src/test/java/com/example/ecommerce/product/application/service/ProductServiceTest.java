package com.example.ecommerce.product.application.service;

import com.example.ecommerce.product.application.port.out.ProductRepositoryPort;
import com.example.ecommerce.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepositoryPort);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // 1. Arrange (Given)
        // Un producto de entrada, sin ID porque aún no se ha guardado.
        Product productToCreate = new Product(null, "Test Product", "A great product", 99.99);

        // El producto que esperamos que el repositorio devuelva, ya con un ID.
        Product savedProduct = new Product(1L, "Test Product", "A great product", 99.99);

        // Configuramos el mock: cuando se llame a save() con cualquier producto,
        // debe devolver nuestro 'savedProduct'.
        when(productRepositoryPort.save(any(Product.class))).thenReturn(savedProduct);

        // 2. Act (When)
        // Llamamos al método que queremos probar.
        Product createdProduct = productService.createProduct(productToCreate);

        // 3. Assert (Then)
        // Verificamos que el resultado no sea nulo y tenga el ID que esperamos.
        assertNotNull(createdProduct);
        assertEquals(1L, createdProduct.getId());
        assertEquals("Test Product", createdProduct.getName());

        // Verificamos que el método save() del repositorio fue llamado exactamente una vez.
        verify(productRepositoryPort).save(any(Product.class));
    }
}