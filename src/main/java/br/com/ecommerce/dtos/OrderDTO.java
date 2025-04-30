package br.com.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}
