package org.mario.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mario.dev.entity.Cart;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private CustomerDto customer;
    private String status;

    public static CartDto mapToDto(Cart cart) {
        return new CartDto(
                cart.getId(),
                CustomerDto.mapToDto(cart.getCustomer()),
                cart.getStatus().name()
        );
    }
}
