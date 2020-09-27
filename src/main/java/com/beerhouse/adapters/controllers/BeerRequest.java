package com.beerhouse.adapters.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
class BeerRequest {
    @NotNull
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String ingredients;

    @NotNull
    private String alcoholContent;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String category;
}
