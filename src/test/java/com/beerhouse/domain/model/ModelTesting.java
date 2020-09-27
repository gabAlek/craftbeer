package com.beerhouse.domain.model;

import com.beerhouse.application.ApplicationTests;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ModelTesting extends ApplicationTests {

    @Test
    public void testBeerConstructor() {
        Beer beer = Beer.builder()
                .id(123)
                .name("Heinz")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();
        assertEquals(123,beer.getId());
        assertEquals("Heinz",beer.getName());
        assertEquals("Lúpulo, água", beer.getIngredients());
        assertEquals("4.5%", beer.getAlcoholContent());
        assertEquals(BigDecimal.valueOf(4.50),beer.getPrice());
        assertEquals("Lager", beer.getCategory());
    }

}
