package com.beerhouse.adapters.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
class BeerRequest {
    @NotNull
    private int id;

    @NotNull
    @Size(min = 2, max = 40)
    private String name;

    @NotNull
    @Size(min = 2, max = 200)
    private String ingredients;

    @NotNull
    @Size(min = 2, max = 6)
    private String alcoholContent;

    @NotNull
    @Min(value = (long) 0.01)
    private BigDecimal price;

    @NotNull
    @Size(min = 1, max = 20)
    private String category;
}
