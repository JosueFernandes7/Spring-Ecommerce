package br.com.ecommerce.controllers;

import br.com.ecommerce.models.CartItem;
import br.com.ecommerce.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Adiciona um item no carrinho
    @PostMapping("/{customerId}/add")
    public ResponseEntity<String> addItem(
            @PathVariable Long customerId,
            @RequestBody CartItem item) {

        cartService.addItem(customerId, item);
        return ResponseEntity.ok("Item adicionado ao carrinho!");
    }

    // Lista todos os itens do carrinho
    @GetMapping("/{customerId}")
    public ResponseEntity<Map<String, CartItem>> getCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    // Atualiza a quantidade de um item
    @PutMapping("/{customerId}/update/{productId}")
    public ResponseEntity<String> updateQuantity(
            @PathVariable Long customerId,
            @PathVariable Long productId,
            @RequestParam int quantity) {

        cartService.updateItemQuantity(customerId, productId, quantity);
        return ResponseEntity.ok("Quantidade atualizada!");
    }

    // Limpa o carrinho (ex: após o cliente finalizar o pedido)
    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Long customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.ok("Carrinho limpo.");
    }

    // Deleta um item único do carrinho
    @DeleteMapping("/{customerId}/remove/{productId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long customerId,
            @PathVariable Long productId) {

        cartService.removeItem(customerId, productId);
        return ResponseEntity.ok("Item removido do carrinho.");
    }
}
