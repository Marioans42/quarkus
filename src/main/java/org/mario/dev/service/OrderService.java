package org.mario.dev.service;

import lombok.extern.slf4j.Slf4j;
import org.mario.dev.dto.AddressDto;
import org.mario.dev.dto.OrderDto;
import org.mario.dev.entity.Order;
import org.mario.dev.entity.enums.OrderStatus;
import org.mario.dev.repository.CartRepository;
import org.mario.dev.repository.OrderRepository;
import org.mario.dev.repository.PaymentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mario.dev.dto.OrderDto.mapToDto;

@Slf4j
@ApplicationScoped
@Transactional
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    CartRepository cartRepository;

    public List<OrderDto> findAll() {
        log.debug("Request to get all Orders");
        return this.orderRepository.findAll()
                .stream()
                .map(OrderDto::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto findById(Long id) {
        log.debug("Request to get Order : {}", id);
        return this.orderRepository.findById(id)
                .map(OrderDto::mapToDto)
                .orElse(null);
    }

    public List<OrderDto> findAllByUser(Long id) {
        return this.orderRepository.findByCartCustomerId(id)
                .stream()
                .map(OrderDto::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto create(OrderDto orderDto) {
        log.debug("Request to create Order : {}", orderDto);

        var cartId = orderDto.getCart().getId();
        var cart = this.cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new IllegalStateException("The Cart with ID[" + cartId + "] was not found !"));


        return mapToDto(
                this.orderRepository.save(
                        new Order(
                                BigDecimal.ZERO, OrderStatus.CREATION,
                                null,
                                null,
                                AddressDto.createFromDto(orderDto.getShipmentAddress()),
                                Collections.emptySet(),
                                cart
                        )
                )
        );
    }

    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);

        var order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order with ID[" + id + "] cannot be found!"));

        Optional.ofNullable(order.getPayment()).ifPresent(paymentRepository::delete);

        orderRepository.delete(order);
    }

    public boolean existsById(Long id) {
        return this.orderRepository.existsById(id);
    }
}
