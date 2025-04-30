package br.com.ecommerce.services;

import br.com.ecommerce.dtos.OrderDTO;
import br.com.ecommerce.dtos.OrderItemDTO;
import br.com.ecommerce.entities.*;
import br.com.ecommerce.models.CartItem;
import br.com.ecommerce.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    private final CartService cartService;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> items = order.getItems().stream().map(item -> new OrderItemDTO(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal())).toList();

        return new OrderDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCreatedAt(),
                items);
    }

    public OrderService(CartService cartService,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            OrderRepository orderRepository) {
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepository.findAllByCustomerId(customerId);
        return orders.stream().map(this::toDTO).toList();
    }
    
    @Transactional
    public OrderDTO checkout(Long customerId) {
        // Buscar cliente
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Recuperar carrinho
        Map<String, CartItem> cart = cartService.getCart(customerId);
        if (cart.isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        // Criar pedido
        Order order = new Order();
        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();

        for (CartItem cartItem : cart.values()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + cartItem.getProductId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            items.add(item);
        }

        order.setItems(items);

        Order saved = orderRepository.save(order);

        cartService.clearCart(customerId);

        return toDTO(saved);
    }
}
