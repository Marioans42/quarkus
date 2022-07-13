package org.mario.dev.service;

import lombok.extern.slf4j.Slf4j;
import org.mario.dev.dto.OrderItemDto;
import org.mario.dev.entity.OrderItem;
import org.mario.dev.repository.OrderItemRepository;
import org.mario.dev.repository.OrderRepository;
import org.mario.dev.repository.ProductRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.mario.dev.dto.OrderItemDto.mapToDto;

@Slf4j
@ApplicationScoped
@Transactional
public class OrderItemService {

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    ProductRepository productRepository;

    public OrderItemDto findById(Long id) {
        log.debug("Request to get OrderItem : {}", id);
        return this.orderItemRepository.findById(id).map(OrderItemDto::mapToDto).orElse(null);
    }

    public OrderItemDto create(OrderItemDto orderItemDto) {
        log.debug("Request to create OrderItem : {}", orderItemDto);
        var order =
                this.orderRepository
                        .findById(orderItemDto.getOrderId())
                        .orElseThrow(() -> new IllegalStateException("The Order does not exist!"));

        var product =
                this.productRepository
                        .findById(orderItemDto.getProductId())
                        .orElseThrow(() -> new IllegalStateException("The Product does not exist!"));

        var orderItem = this.orderItemRepository.save(
                new OrderItem(
                        orderItemDto.getQuantity(),
                        product,
                        order
                ));

        order.setPrice(order.getPrice().add(orderItem.getProduct().getPrice()));

        this.orderRepository.save(order);

        return mapToDto(orderItem);
    }

    public void delete(Long id) {
        log.debug("Request to delete OrderItem : {}", id);

        var orderItem = this.orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("The OrderItem does not exist!"));

        var order = orderItem.getOrder();
        order.setPrice(
                order.getPrice().subtract(orderItem.getProduct().getPrice())
        );

        this.orderItemRepository.deleteById(id);

        order.getOrderItems().remove(orderItem);

        this.orderRepository.save(order);
    }

    public List<OrderItemDto> findByOrderId(Long id) {
        log.debug("Request to get all OrderItems of OrderId {}", id);
        return this.orderItemRepository.findAllByOrderId(id)
                .stream()
                .map(OrderItemDto::mapToDto)
                .collect(Collectors.toList());
    }
}
