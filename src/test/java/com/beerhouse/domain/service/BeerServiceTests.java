package com.beerhouse.domain.service;

import com.beerhouse.application.config.BeanConfig;
import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.model.exception.BeerAlreadyExistsException;
import com.beerhouse.domain.model.exception.BeerNotFoundException;
import com.beerhouse.domain.model.exception.NoBeersFoundException;
import com.beerhouse.domain.ports.BeerPersistencePort;
import com.beerhouse.domain.ports.BeerRepositoryPort;
import com.beerhouse.domain.services.BeerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        BeerService.class,
        BeanConfig.class
})
public class BeerServiceTests {

    @MockBean
    BeerRepositoryPort beerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BeerPersistencePort beerService;

    @Test
    public void returnSuccess_retrieveOne() {

        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerRepository.retrieveOne(1)).thenReturn(beer);

        Beer result = beerService.retrieveOne(1);

        assertTrue(beer.equals(result));
    }

    @Test
    public void returnFailure_retrieveOne_notFound() {

        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerRepository.retrieveOne(1)).thenReturn(null);

        try{
            Beer result = beerService.retrieveOne(1);
        } catch (Exception ex){
            assertTrue(ex instanceof BeerNotFoundException);
        }
    }

    @Test
    public void returnSuccess_retrieveMany() {
        Beer beer1 = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Beer beer2 = Beer.builder()
                .id(2)
                .name("Eisenbahn")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.20))
                .category("Lager")
                .build();

        List<Beer> expectedResultsList = new ArrayList<>(Arrays.asList(beer1,beer2));

        Mockito.when(beerRepository.retrieveMany()).thenReturn(expectedResultsList);

        List<Beer> actualResultsList = beerRepository.retrieveMany();

        assertEquals(actualResultsList,expectedResultsList);
    }

    @Test
    public void returnFailure_retrieveMany_NoneFound() {

        Mockito.when(beerRepository.retrieveMany()).thenReturn(null);

        try{
            List<Beer> actualResultsList = beerRepository.retrieveMany();
        } catch (Exception ex) {
            assertTrue(ex instanceof NoBeersFoundException);
        }
    }

    @Test
    public void returnSuccess_create() {
        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerRepository.create(beer)).thenReturn(beer);

        Beer insertedBeer = beerRepository.create(beer);

        assertEquals(beer.getName(),insertedBeer.getName());
        assertEquals(beer.getIngredients(),insertedBeer.getIngredients());
        assertEquals(beer.getAlcoholContent(),insertedBeer.getAlcoholContent());
        assertEquals(beer.getPrice(),insertedBeer.getPrice());
        assertEquals(beer.getCategory(),insertedBeer.getCategory());
    }

    @Test
    public void returnFailure_create_nameAlreadyExists() {
        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerRepository.nameExists(beer.getName())).thenReturn(true);

        try{
            Beer insertedBeer = beerRepository.create(beer);
        } catch (Exception ex) {
            assertTrue(ex instanceof BeerAlreadyExistsException);
        }
    }

    @Test
    public void returnSuccess_update() {
        Beer replacementBeer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Beer existingBeer = Beer.builder()
                .id(1)
                .name("Eisenbahn")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.20))
                .category("Lager")
                .build();

        Mockito.when(beerRepository.retrieveOne(replacementBeer.getId())).thenReturn(existingBeer);

        Mockito.when(beerRepository.update(replacementBeer)).thenReturn(replacementBeer);

        Beer updatedBeer = beerService.update(1,replacementBeer);

        assertEquals(updatedBeer,replacementBeer);
    }

    @Test
    public void returnFailure_update_beerDoesntExist() {
        Beer replacementBeer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerRepository.retrieveOne(replacementBeer.getId())).thenReturn(null);

        try {
            Beer updatedBeer = beerService.update(1,replacementBeer);
        } catch(Exception ex) {
            assertTrue(ex instanceof BeerNotFoundException);
        }
    }

    @Test
    public void returnSuccess_alter() {
        LinkedHashMap<String,Object> fields = new LinkedHashMap<>();
        fields.put("alcoholContent","3.0%");
        fields.put("category","Pilsen");
        fields.put("name","Brahma");

        Beer existingBeer = Beer.builder()
                .id(1)
                .name("Eisenbahn")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.20))
                .category("Lager")
                .build();

        Beer expectedBeer = Beer.builder()
                .id(1)
                .name("Brahma")
                .ingredients("Lúpulo, água")
                .alcoholContent("3.0%")
                .price(BigDecimal.valueOf(4.20))
                .category("Pilsen")
                .build();

        Mockito.when(beerRepository.retrieveOne(1)).thenReturn(existingBeer);
        Mockito.when(beerRepository.alter(expectedBeer)).thenReturn(expectedBeer);

        Beer alteredBeer = beerService.alter(1,fields);
        assertEquals(expectedBeer,alteredBeer);
    }

    @Test
    public void returnFailure_alter_beerDoesntExist() {
        LinkedHashMap<String,Object> fields = new LinkedHashMap<>();
        fields.put("alcoholContent","3.0%");
        fields.put("category","Pilsen");
        fields.put("name","Brahma");


        Beer expectedBeer = Beer.builder()
                .id(1)
                .name("Brahma")
                .ingredients("Lúpulo, água")
                .alcoholContent("3.0%")
                .price(BigDecimal.valueOf(4.20))
                .category("Pilsen")
                .build();

        Mockito.when(beerRepository.retrieveOne(1)).thenReturn(null);

        try {
            Beer altered = beerService.alter(1,fields);
        } catch(Exception ex) {
            assertTrue(ex instanceof BeerNotFoundException);
        }
    }


    @Test
    public void returnSuccess_delete() {

        Beer expectedBeer = Beer.builder()
                .id(1)
                .name("Brahma")
                .ingredients("Lúpulo, água")
                .alcoholContent("3.0%")
                .price(BigDecimal.valueOf(4.20))
                .category("Pilsen")
                .build();

        Mockito.when(beerRepository.delete(1)).thenReturn(1L);

        Long result = beerService.delete(1);

        assertEquals(1L, result.longValue());
    }

    @Test
    public void returnFailure_delete_notFound() {

        Beer expectedBeer = Beer.builder()
                .id(1)
                .name("Brahma")
                .ingredients("Lúpulo, água")
                .alcoholContent("3.0%")
                .price(BigDecimal.valueOf(4.20))
                .category("Pilsen")
                .build();

        Mockito.when(beerRepository.delete(1)).thenReturn(0L);

        try {
            Long result = beerService.delete(1);
        }catch (Exception ex) {
            assertTrue(ex instanceof BeerNotFoundException);
        }
    }
}
