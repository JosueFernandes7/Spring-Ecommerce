package br.com.ecommerce.controllers;

import br.com.ecommerce.dtos.OrderDTO;
import br.com.ecommerce.services.OrderService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout/{customerId}")
    public ResponseEntity<OrderDTO> checkout(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.checkout(customerId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

}
