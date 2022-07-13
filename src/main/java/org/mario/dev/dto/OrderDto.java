package org.mario.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mario.dev.entity.Order;
import org.mario.dev.entity.OrderItem;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private BigDecimal totalPrice;
    private String status;
    private ZonedDateTime shipped;
    private Long paymentId;
    private AddressDto shipmentAddress;
    private Set<OrderItemDto> orderItems;
    private CartDto cart;

    public static OrderDto mapToDto(Order order) {
        var orderItems = order
                .getOrderItems()
                .stream()
                .map(OrderItemDto::mapToDto)
                .collect(Collectors.toSet());

        return new OrderDto(
                order.getId(),
                order.getPrice(),
                order.getStatus().name(),
                order.getShipped(),
                order.getPayment() != null ? order.getPayment().getId() : null,
                AddressDto.mapToDto(order.getShipmentAddress()),
                orderItems,
                CartDto.mapToDto(order.getCart())
        );
    }
}
