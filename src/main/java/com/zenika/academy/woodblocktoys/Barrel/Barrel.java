package com.zenika.academy.woodblocktoys.Barrel;

import com.zenika.academy.woodblocktoys.Block.Block;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

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

    @Column(name = "barrel_price")
    private double price;

    @Column(name = "barrel_quantity")
    private int blockQuantity;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Block> blockList;

}