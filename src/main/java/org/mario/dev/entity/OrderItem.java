package org.mario.dev.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "order_items")
public class OrderItem extends AbstractEntity{
    @NotNull
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Order order;
}
