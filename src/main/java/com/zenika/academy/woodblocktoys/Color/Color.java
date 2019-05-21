package com.zenika.academy.woodblocktoys.Color;

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
@Table(name = "main_blocks_color")
public class Color implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "color_type", unique = true)
    private String type;

}