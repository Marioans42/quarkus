package org.mario.dev.service;



import lombok.extern.slf4j.Slf4j;
import org.mario.dev.dto.CartDto;
import org.mario.dev.entity.Cart;
import org.mario.dev.entity.enums.CartStatus;
import org.mario.dev.repository.CartRepository;
import org.mario.dev.repository.CustomerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class CartService {

    @Inject
    CartRepository cartRepository;

    @Inject
    CustomerRepository customerRepository;

    public List<CartDto> findAll() {
        log.debug("Request to get all Carts");
        return this.cartRepository.findAll()
                .stream()
                .map(CartDto::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CartDto> findAllActiveCart() {
        return this.cartRepository.findByStatus(CartStatus.NEW)
                .stream()
                .map(CartDto::mapToDto)
                .collect(Collectors.toList());
    }

    public Cart create(Long customerId) {
        if(this.getActiveCart(customerId) == null) {
            var customer = this.customerRepository.findById(customerId).
                    orElseThrow(() -> new IllegalStateException("The Customer does not exist !!!"));
            var cart = new Cart(customer, CartStatus.NEW);
            return this.cartRepository.save(cart);
        } else {
            throw new IllegalStateException("There is already an active cart");
        }
    }

    public CartDto createDto(Long customerId) {
        return CartDto.mapToDto(this.create(customerId));
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public CartDto findById(Long id) {
        log.debug("Request to get Cart: {}", id);
        return this.cartRepository.findById(id).map(CartDto::mapToDto).orElse(null);
    }

    public void delete(Long id) {
        log.debug("Request to delete Cart : {}", id);
        Cart cart = this.cartRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(" Cannot find cart with id " + id));
        cart.setStatus(CartStatus.CANCELED);
        cartRepository.save(cart);
    }

    public CartDto getActiveCart(Long customerId) {
        List<Cart> carts = this.cartRepository.findByStatusAndCustomerId(CartStatus.NEW, customerId);
        if(carts != null) {
            if(carts.size() == 1) {
                return CartDto.mapToDto(carts.get(0));
            }
            if(carts.size() > 1) {
                throw new IllegalStateException("Many active carts detected !!");
            }
        }
        return null;
    }

    public List<CartDto> findAllActiveCarts() {
        return this.cartRepository.findByStatus(CartStatus.NEW)
                .stream()
                .map(CartDto::mapToDto)
                .collect(Collectors.toList());
    }
}
