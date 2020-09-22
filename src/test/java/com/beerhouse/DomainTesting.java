package com.beerhouse;

import com.beerhouse.application.Application;
import com.beerhouse.domain.model.Beer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ComponentScan(basePackageClasses = Application.class)
public class DomainTesting {

    @Test
    public void testBeerConstructor() {
        Beer beer = new Beer(123,"Heinz", "Lúpulo, água", "4.5%", BigDecimal.valueOf(4.50),"Lager");
        assertEquals(123,beer.getId());
        assertEquals("Heinz",beer.getName());
        assertEquals("Lúpulo, água", beer.getIngredients());
        assertEquals("4.5%", beer.getAlcoholContent());
        assertEquals(BigDecimal.valueOf(4.50),beer.getPrice());
        assertEquals("Lager", beer.getCategory());
    }
}
