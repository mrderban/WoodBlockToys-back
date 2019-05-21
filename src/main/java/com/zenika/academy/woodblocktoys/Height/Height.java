package com.zenika.academy.woodblocktoys.Height;

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
@Table(name = "main_blocks_height")
public class Height implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "height_value", unique = true)
    private double value;

}