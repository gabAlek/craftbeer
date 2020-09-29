package com.beerhouse.domain.model;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Beer {
    private int id;
    private String name;
    private String ingredients;
    private String alcoholContent;
    private BigDecimal price;
    private String category;

    public void setPrice(BigDecimal price) {
        if(price.compareTo(BigDecimal.ZERO) == 1) {
            this.price = price;
        } else
            throw new IllegalArgumentException("Price must be greater than zero");
    }
}