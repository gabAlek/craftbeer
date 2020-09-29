package com.beerhouse.adapters.respository;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "beer")
public class BeerEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true,updatable = false,nullable = false)
    private Integer id;

    @Column(unique = true,nullable = false)
    private String name;

    @Column(nullable = false)
    private String ingredients;

    @Column(nullable = false)
    private String alcoholContent;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String category;

}
