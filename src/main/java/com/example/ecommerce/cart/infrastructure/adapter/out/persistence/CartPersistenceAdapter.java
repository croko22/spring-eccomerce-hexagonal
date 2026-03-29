package com.example.ecommerce.cart.infrastructure.adapter.out.persistence;

import com.example.ecommerce.cart.application.port.out.CartRepositoryPort;
import com.example.ecommerce.cart.domain.model.Cart;
import com.example.ecommerce.cart.domain.model.CartItem;
import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.infrastructure.adapter.out.persistence.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CartPersistenceAdapter implements CartRepositoryPort {

    private final CartJpaRepository cartJpaRepository;

    public CartPersistenceAdapter(CartJpaRepository cartJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = new CartEntity();
        if (cart.getId() != null) {
            entity.setId(cart.getId());
        }
        entity.setUserId(cart.getUserId());

        List<CartItemEntity> itemEntities = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItemEntity itemEntity = new CartItemEntity();
            if (item.getId() != null) {
                itemEntity.setId(item.getId());
            }
            itemEntity.setProduct(mapToProductEntity(item.getProduct()));
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setCart(entity);
            itemEntities.add(itemEntity);
        }
        entity.setItems(itemEntities);

        CartEntity savedEntity = cartJpaRepository.save(entity);

        return mapToDomainModel(savedEntity);
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartJpaRepository.findByUserId(userId)
                .map(this::mapToDomainModel);
    }

    @Override
    public void deleteByUserId(Long userId) {
        cartJpaRepository.findByUserId(userId).ifPresent(cartJpaRepository::delete);
    }

    private Cart mapToDomainModel(CartEntity entity) {
        List<CartItem> items = entity.getItems().stream()
                .map(this::mapToCartItem)
                .collect(Collectors.toList());

        return new Cart(entity.getId(), entity.getUserId(), items);
    }

    private CartItem mapToCartItem(CartItemEntity entity) {
        Product product = mapToProductDomain(entity.getProduct());
        return new CartItem(entity.getId(), product, entity.getQuantity());
    }

    private Product mapToProductDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice()
        );
    }

    private ProductEntity mapToProductEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        if (product.getId() != null) {
            entity.setId(product.getId());
        }
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        return entity;
    }
}
