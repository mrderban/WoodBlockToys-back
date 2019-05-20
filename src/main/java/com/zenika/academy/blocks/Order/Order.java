package com.zenika.academy.blocks.Order;

import com.zenika.academy.blocks.Account.Account;
import com.zenika.academy.blocks.Barrel.Barrel;
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

@Entity // entité JPA
@Table(name = "main_blocks_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "order_date")
    private LocalDate date;

    @Column(name = "order_payment_state")
    private Boolean isPaid;

    @Column(name = "order_validation_state")
    private Boolean isValidated;

    @Column(name = "order_building_state")
    private Boolean isBuilt;

    @Column(name = "order_send_state")
    private Boolean isSent;

    @Column(name = "order_reception_state")
    private Boolean isReceived;

    @ManyToOne
    private Account account;

    @OneToOne
    private Barrel barrel;
}