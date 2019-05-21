package com.zenika.academy.woodblocktoys.Shape;

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
@Table(name = "main_blocks_shape")
public class Shape implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;


    @Column(name = "shape_type")
    private String type;

}