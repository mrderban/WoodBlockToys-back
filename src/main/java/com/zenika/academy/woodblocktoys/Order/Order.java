package com.zenika.academy.woodblocktoys.Order;

import com.zenika.academy.woodblocktoys.Account.Account;
import com.zenika.academy.woodblocktoys.Barrel.Barrel;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter

@Entity
@Table(name = "main_blocks_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "order_date")
    private LocalDate createdAt;

    @Column(name = "order_price")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, name = "order_state")
    private OrderState orderState;

    @ManyToOne
    private Account account;

    @OneToOne
    private Barrel barrel;
}