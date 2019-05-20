package com.zenika.academy.woodblocktoys.Barrel;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter

@Entity
@Table(name = "main_blocks_barrel")
public class Barrel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "barrel_reference", unique = true)
    private String reference;

    @Column(name = "barrel_price")
    private double price;

    @Column(name = "barrel_quantity")
    private int blockQuantity;
}