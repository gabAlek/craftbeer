package com.beerhouse.adapters.controllers;

import com.beerhouse.IncompleteObject;
import com.beerhouse.application.config.BeanConfig;
import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.ports.BeerPersistencePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        BeerController.class,
        BeanConfig.class,
        ObjectMapper.class
})
public class BeerControllerTests {

    @Autowired
    private BeerController beerController;

    @MockBean
    private BeerPersistencePort beerService;

    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(beerController)
                .build();
    }

    @Test
    public void returnSuccess_retrieveBeer() throws Exception {
        mvc.perform(get("/beers/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void returnSuccess_retrieveBeers() throws Exception {
        mvc.perform(get("/beers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print()).andExpect(status().isOk());
    }


    @Test
    public void returnSuccess_createBeer() throws Exception {
        Beer beer = Beer.builder()
                .id(123)
                .name("Heinz")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

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
                .id(123)
                .name("Heinz")
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
    public void returnSuccess_updateBeer() throws Exception {
        Beer beer = Beer.builder()
                .id(123)
                .name("Heinz")
                .ingredients("Lúpulo, água")
                .alcoholContent("4.5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

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
                .id(123)
                .name("Heinz")
                .ingredients("Lúpulo, água")
                .alcoholContent(null)
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

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
                .id(123)
                .name("Heinz")
                .ingredients("Lúpulo, água")
                .alcoholContent("4,5%")
                .price(BigDecimal.valueOf(4.50))
                .category("Lager")
                .build();

        mvc.perform(patch("/beers/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void returnSuccess_alterBeerWithIncompleteBody() throws Exception {
        Beer beer = Beer.builder()
                .id(123)
                .name("Heinz")
                .ingredients("Lúpulo, água")
                .alcoholContent(null)
                .price(null)
                .category(null)
                .build();

        IncompleteObject obj = IncompleteObject.builder()
                .category("aaaa")
                .id(2)
                .name("bbb")
                .build();

        mvc.perform(patch("/beers/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj))
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
}
