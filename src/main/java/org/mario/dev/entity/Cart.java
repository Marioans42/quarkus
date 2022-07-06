package org.mario.dev.entity;

import lombok.*;
import org.mario.dev.entity.enums.CartStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart extends AbstractEntity{
    @ManyToOne
    private Customer customer;
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus status;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(customer, cart.customer) &&
                status == cart.status;
    }
    @Override
    public int hashCode() {
        return Objects.hash(customer, status);
    }
}
