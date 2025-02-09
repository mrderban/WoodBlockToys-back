package com.zenika.academy.woodblocktoys.Block;

import com.zenika.academy.woodblocktoys.Color.Color;
import com.zenika.academy.woodblocktoys.Finition.Finition;
import com.zenika.academy.woodblocktoys.Height.Height;
import com.zenika.academy.woodblocktoys.Shape.Shape;
import com.zenika.academy.woodblocktoys.Wood.Wood;
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
@Table(name = "main_blocks_block")
public class Block implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "block_price")
    private double price;

    @Column(name = "block_volume")
    private double volume; // in cm^3

    @Column(name = "block_area")
    private double area; // in cm²

    @Column(name = "block_first_dim")
    private double firstDim; // in cm

    @Column(name = "block_second_dim")
    private double secondDim; // in cm

    @OneToOne
    private Color color;

    @OneToOne
    private Height height;

    @OneToOne
    private Shape shape;

    @OneToOne
    private Finition finition;

    @OneToOne
    private Wood wood;


}
