package com.zenika.academy.woodblocktoys.Wood;

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
@Table(name = "main_blocks_wood")
public class Wood implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "wood_price")
    private double volumePrice;

    @Column(name = "wood_type", unique = true)
    private String type;

}