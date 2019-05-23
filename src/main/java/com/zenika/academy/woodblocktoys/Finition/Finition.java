package com.zenika.academy.woodblocktoys.Finition;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter

@Entity
@Table(name = "main_blocks_paint")
public class Finition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "paint_price")
    private double surfacePrice;

    @Column(name = "paint_type")
    @Pattern(regexp = "[^0-9]+", message = "Digits are not allowed")
    private String type;

}