package com.beerhouse.adapters.controllers;

import com.beerhouse.application.config.BeanConfig;
import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.model.exception.BeerAlreadyExistsException;
import com.beerhouse.domain.model.exception.BeerNotFoundException;
import com.beerhouse.domain.model.exception.NoBeersFoundException;
import com.beerhouse.domain.ports.BeerPersistencePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        BeerController.class,
        BeanConfig.class,
        ObjectMapper.class,
        BeerControllerAdvice.class
})
public class BeerControllerTests {

    @Autowired
    private BeerController beerController;

    @MockBean
    private BeerPersistencePort beerService;

    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BeerControllerAdvice beerControllerAdvice;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(beerController)
                .setControllerAdvice(beerControllerAdvice)
                .build();
    }

    @Test
    public void returnSuccess_retrieveBeer() throws Exception {
        Mockito.when(beerService.retrieveOne(1)).thenReturn(Beer.builder().build());
        mvc.perform(get("/beers/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void returnFailure_retrieveBeer_notFound() throws Exception {
        Mockito.when(beerService.retrieveOne(1)).thenThrow(new BeerNotFoundException(1));
        mvc.perform(get("/beers/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof BeerNotFoundException))
                .andExpect(result -> assertEquals("Could not find beer with id 1",
                        result.getResolvedException().getMessage()));
    }


    @Test
    public void returnSuccess_retrieveBeers() throws Exception {
        mvc.perform(get("/beers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isOk());
    }


    @Test
    public void returnFailure_retrieveBeers_noneFound() throws Exception {
        Mockito.when(beerService.retrieveMany()).thenThrow(new NoBeersFoundException());
        mvc.perform(get("/beers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof NoBeersFoundException))
                .andExpect(result -> assertEquals("No beers were found"
                        ,result.getResolvedException().getMessage()));
    }


    @Test
    public void returnSuccess_createBeer() throws Exception {

        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerService.create(beer)).thenReturn(beer);

        mvc.perform(post("/beers").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void returnFailure_createBeerWithIncompleteBody() throws Exception {

        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category(null)
                .build();

        mvc.perform(post("/beers").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnFailure_createBeer_nameAlreadyExists() throws Exception {

        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerService.create(beer)).thenThrow(new BeerAlreadyExistsException(beer.getName()));

        mvc.perform(post("/beers").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof BeerAlreadyExistsException))
                .andExpect(result -> assertEquals(
                        "Beer with name " + beer.getName() + " already exists",
                         result.getResolvedException().getMessage()));
    }

    @Test
    public void returnSuccess_updateBeer() throws Exception {
        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerService.update(1, beer)).thenReturn(beer);

        mvc.perform(put("/beers/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void returnFailure_updateBeerWithIncompleteBody() throws Exception {
        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent(null)
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        Mockito.when(beerService.update(1, beer)).thenReturn(beer);

        mvc.perform(put("/beers/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnSuccess_alterBeer() throws Exception {
        Beer beer = Beer.builder()
                .id(1)
                .name("Heineken")
                .ingredients("Lúpulo, água")
                .alcoholContent(null)
                .price(null)
                .category(null)
                .build();

        LinkedHashMap<String,Object> fields = new LinkedHashMap<>();
        fields.put("alcoholContent","3.0%");
        fields.put("category","Pilsen");
        fields.put("name","Brahma");

        mvc.perform(patch("/beers/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fields))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void returnSuccess_deleteBeer() throws Exception {

        mvc.perform(delete("/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void returnFailure_deleteBeer_notFound() throws Exception {
        Mockito.when(beerService.delete(1)).thenThrow(new BeerNotFoundException(1));
        mvc.perform(delete("/beers/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof BeerNotFoundException))
                .andExpect(result -> assertEquals("Could not find beer with id 1",
                        result.getResolvedException().getMessage()));
    }
}
