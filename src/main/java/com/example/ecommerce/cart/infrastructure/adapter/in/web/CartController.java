package com.example.ecommerce.cart.infrastructure.adapter.in.web;

import com.example.ecommerce.cart.application.port.in.*;
import com.example.ecommerce.cart.domain.model.Cart;
import com.example.ecommerce.cart.infrastructure.adapter.in.web.dto.AddToCartRequest;
import com.example.ecommerce.cart.infrastructure.adapter.in.web.dto.CartResponse;
import com.example.ecommerce.cart.infrastructure.adapter.in.web.dto.UpdateCartItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Tag(name = "Shopping Cart", description = "Shopping cart management endpoints")
public class CartController {

    private final AddToCartUseCase addToCartUseCase;
    private final RemoveFromCartUseCase removeFromCartUseCase;
    private final GetCartUseCase getCartUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final UpdateCartItemUseCase updateCartItemUseCase;

    public CartController(AddToCartUseCase addToCartUseCase,
                          RemoveFromCartUseCase removeFromCartUseCase,
                          GetCartUseCase getCartUseCase,
                          ClearCartUseCase clearCartUseCase,
                          UpdateCartItemUseCase updateCartItemUseCase) {
        this.addToCartUseCase = addToCartUseCase;
        this.removeFromCartUseCase = removeFromCartUseCase;
        this.getCartUseCase = getCartUseCase;
        this.clearCartUseCase = clearCartUseCase;
        this.updateCartItemUseCase = updateCartItemUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add item to cart", description = "Adds a product to the user's cart")
    public CartResponse addToCart(@RequestParam Long userId, @RequestBody AddToCartRequest request) {
        Cart cart = addToCartUseCase.addToCart(userId, request.getProductId(), request.getQuantity());
        return CartResponse.fromDomain(cart);
    }

    @GetMapping
    @Operation(summary = "Get cart", description = "Gets the user's cart")
    public CartResponse getCart(@RequestParam Long userId) {
        Cart cart = getCartUseCase.getCartByUserId(userId);
        return CartResponse.fromDomain(cart);
    }

    @DeleteMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove item from cart", description = "Removes a product from the user's cart")
    public void removeFromCart(@RequestParam Long userId, @PathVariable Long productId) {
        removeFromCartUseCase.removeFromCart(userId, productId);
    }

    @PutMapping("/items/{productId}")
    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of a product in the cart")
    public CartResponse updateCartItem(@RequestParam Long userId, 
                                        @PathVariable Long productId, 
                                        @RequestBody UpdateCartItemRequest request) {
        Cart cart = updateCartItemUseCase.updateCartItemQuantity(userId, productId, request.getQuantity());
        return CartResponse.fromDomain(cart);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Clear cart", description = "Clears all items from the user's cart")
    public void clearCart(@RequestParam Long userId) {
        clearCartUseCase.clearCart(userId);
    }
}
