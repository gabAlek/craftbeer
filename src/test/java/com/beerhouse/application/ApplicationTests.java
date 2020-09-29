package com.beerhouse.application;

import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.model.exception.BeerAlreadyExistsException;
import com.beerhouse.domain.model.exception.BeerNotFoundException;
import com.beerhouse.domain.model.exception.NoBeersFoundException;
import com.beerhouse.domain.ports.BeerPersistencePort;
import com.beerhouse.domain.ports.BeerRepositoryPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@TestPropertySource("classpath:application.beer-integration-tests.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private BeerRepositoryPort beerRepository;

	@Autowired
	private BeerPersistencePort beerService;

	@Autowired
	ObjectMapper objectMapper;


	@Test
	public void contextLoads() {
	}

	@Test
	public void returnSuccess_retrieveBeer() throws Exception {
		Beer beer = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		beerRepository.create(beer);

		String beerJson = objectMapper.writeValueAsString(beer);

		mvc.perform(get("/beers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id",is(beer.getId())))
				.andExpect(jsonPath("$.name",is(beer.getName())))
				.andExpect(jsonPath("$.ingredients",is(beer.getIngredients())))
				.andExpect(jsonPath("$.alcoholContent",is(beer.getAlcoholContent())))
				.andExpect(jsonPath("$.price",is(beer.getPrice().doubleValue())))
				.andExpect(jsonPath("$.category",is(beer.getCategory())));
	}

	@Test
	public void returnFailure_retrieveBeer_beerDoesntExists() throws Exception {

		mvc.perform(get("/beers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException()
						instanceof BeerNotFoundException))
				.andExpect(result -> assertEquals("Could not find beer with id 1",
						result.getResolvedException().getMessage()));
	}


	@Test
	public void returnSuccess_retrieveBeers() throws Exception {
		Beer beer1 = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		beerRepository.create(beer1);

		Beer beer2 = Beer.builder().id(2).name("Eisenbahn").ingredients("Lúpulo, água")
				.alcoholContent("4.0%").price(BigDecimal.valueOf(4.20)).category("Lager")
				.build();

		beerRepository.create(beer2);

		mvc.perform(get("/beers")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id",is(beer1.getId())))
				.andExpect(jsonPath("$[0].name",is(beer1.getName())))
				.andExpect(jsonPath("$[0].ingredients",is(beer1.getIngredients())))
				.andExpect(jsonPath("$[0].alcoholContent",is(beer1.getAlcoholContent())))
				.andExpect(jsonPath("$[0].price",is(beer1.getPrice().doubleValue())))
				.andExpect(jsonPath("$[0].category",is(beer1.getCategory())))
				.andExpect(jsonPath("$[1].id",is(beer2.getId())))
				.andExpect(jsonPath("$[1].name",is(beer2.getName())))
				.andExpect(jsonPath("$[1].ingredients",is(beer2.getIngredients())))
				.andExpect(jsonPath("$[1].alcoholContent",is(beer2.getAlcoholContent())))
				.andExpect(jsonPath("$[1].price",is(beer2.getPrice().doubleValue())))
				.andExpect(jsonPath("$[1].category",is(beer2.getCategory())));
	}


	@Test
	public void returnFailure_retrieveBeers_noneFound() throws Exception {

		mvc.perform(get("/beers")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof NoBeersFoundException))
				.andExpect(result -> assertEquals("No beers were found"
						,result.getResolvedException().getMessage()));
	}


	@Test
	public void returnSuccess_createBeer() throws Exception {
		Beer beer = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		mvc.perform(post("/beers").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id",is(beer.getId())))
				.andExpect(jsonPath("$.name",is(beer.getName())))
				.andExpect(jsonPath("$.ingredients",is(beer.getIngredients())))
				.andExpect(jsonPath("$.alcoholContent",is(beer.getAlcoholContent())))
				.andExpect(jsonPath("$.price",is(beer.getPrice().doubleValue())))
				.andExpect(jsonPath("$.category",is(beer.getCategory())))
				.andExpect(status().isCreated());
	}

	@Test
	public void returnFailure_createBeer_nameAlreadyExists() throws Exception {

		Beer beer = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		beerRepository.create(beer);

		mvc.perform(post("/beers").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BeerAlreadyExistsException))
				.andExpect(result -> assertEquals(
						"Beer with name " + beer.getName() + " already exists",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void returnSuccess_updateBeer() throws Exception {
		Beer existingBeer = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		beerRepository.create(existingBeer);

		Beer replacementBeer = Beer.builder().id(1).name("Eisenbahn").ingredients("Lúpulo, água")
				.alcoholContent("4.0%").price(BigDecimal.valueOf(4.20)).category("Lager")
				.build();

		mvc.perform(put("/beers/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(replacementBeer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id",is(replacementBeer.getId())))
				.andExpect(jsonPath("$.name",is(replacementBeer.getName())))
				.andExpect(jsonPath("$.ingredients",is(replacementBeer.getIngredients())))
				.andExpect(jsonPath("$.alcoholContent",is(replacementBeer.getAlcoholContent())))
				.andExpect(jsonPath("$.price",is(replacementBeer.getPrice().doubleValue())))
				.andExpect(jsonPath("$.category",is(replacementBeer.getCategory())));
	}

	@Test
	public void returnFailure_updateBeer_beerDoesntExists() throws Exception {
		Beer replacementBeer = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		mvc.perform(put("/beers/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(replacementBeer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException()
						instanceof BeerNotFoundException))
				.andExpect(result -> assertEquals("Could not find beer with id 1",
						result.getResolvedException().getMessage()));
	}


	@Test
	public void returnFailure_updateBeer_nameAlreadyExists() throws Exception {
		Beer existingBeer1 = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		beerRepository.create(existingBeer1);

		Beer existingBeer2 = Beer.builder().id(2).name("Eisenbahn").ingredients("Lúpulo, água")
				.alcoholContent("4.0%").price(BigDecimal.valueOf(4.20)).category("Lager")
				.build();

		beerRepository.create(existingBeer2);

		Beer replacementBeer = Beer.builder().id(1).name("Eisenbahn").ingredients("Água, milho")
				.alcoholContent("2.5%").price(BigDecimal.valueOf(3.90)).category("Pilsen")
				.build();

		mvc.perform(put("/beers/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(replacementBeer))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BeerAlreadyExistsException))
				.andExpect(result -> assertEquals(
						"Beer with name " + replacementBeer.getName() + " already exists",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void returnSuccess_alterBeer() throws Exception {
		Beer beer = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		beerRepository.create(beer);

		LinkedHashMap<String,Object> fields = new LinkedHashMap<>();
		fields.put("alcoholContent","3.0%");
		fields.put("category","Pilsen");
		fields.put("name","Brahma");

		mvc.perform(patch("/beers/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(fields))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id",is(beer.getId())))
				.andExpect(jsonPath("$.name",is(fields.get("name"))))
				.andExpect(jsonPath("$.ingredients",is(beer.getIngredients())))
				.andExpect(jsonPath("$.alcoholContent",is(fields.get("alcoholContent"))))
				.andExpect(jsonPath("$.price",is(beer.getPrice().doubleValue())))
				.andExpect(jsonPath("$.category",is(fields.get("category"))));
	}

	@Test
	public void returnSuccess_alterBeer_beerDoesntExists() throws Exception {

		LinkedHashMap<String,String> fields = new LinkedHashMap<>();
		fields.put("name","Eisenbahn");

		mvc.perform(patch("/beers/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(fields))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertTrue(result.getResolvedException()
						instanceof BeerNotFoundException))
				.andExpect(result -> assertEquals("Could not find beer with id 1",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void returnSuccess_alterBeer_nameAlreadyExists() throws Exception {
		Beer existingBeer1 = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		beerRepository.create(existingBeer1);

		Beer existingBeer2 = Beer.builder().id(2).name("Eisenbahn").ingredients("Lúpulo, água")
				.alcoholContent("4.0%").price(BigDecimal.valueOf(4.20)).category("Lager")
				.build();

		beerRepository.create(existingBeer2);

		LinkedHashMap<String,String> fields = new LinkedHashMap<>();
		fields.put("name","Eisenbahn");

		mvc.perform(patch("/beers/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(fields))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(
						result.getResolvedException() instanceof BeerAlreadyExistsException))
				.andExpect(result -> assertEquals(
						"Beer with name " + fields.get("name") + " already exists",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void returnSuccess_deleteBeer() throws Exception {

		Beer beer = Beer.builder().id(1).name("Heineken").ingredients("Lúpulo, água")
				.alcoholContent("4.5%").price(BigDecimal.valueOf(4.50)).category("Lager")
				.build();

		beerRepository.create(beer);

		mvc.perform(delete("/beers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertEquals("1 row(s) deleted" ,result.getResponse().getContentAsString()));
	}

	@Test
	public void returnFailure_deleteBeer_beerDoesntExists() throws Exception {

		mvc.perform(delete("/beers/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertTrue(result.getResolvedException()
						instanceof BeerNotFoundException))
				.andExpect(result -> assertEquals("Could not find beer with id 1",
						result.getResolvedException().getMessage()));
	}

}