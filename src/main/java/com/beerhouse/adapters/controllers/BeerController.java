package com.beerhouse.adapters.controllers;

import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.ports.BeerPersistencePort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/beers",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class BeerController {

    @Autowired
    private BeerPersistencePort beerService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<Beer> retrieveAllBeers() {
        return beerService.retrieveMany();
    }

    @GetMapping(path = "/{id}")
    public Beer retrieveBeer(@PathVariable String id) {
        int parsedId = Integer.parseInt(id);
        return beerService.retrieveOne(parsedId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Beer createBeer(@Valid @RequestBody BeerRequest body) {
        return beerService.create(modelMapper.map(body, Beer.class));
    }

    @PutMapping(path = "/{id}")
    public Beer updateBeer(@PathVariable String id,
                           @Valid @RequestBody BeerRequest body) {
        return beerService.update(modelMapper.map(body, Beer.class));
    }

    @PatchMapping(path = "/{id}")
    public Beer alterBeer(@PathVariable String id,
                                            @RequestBody Object fields) {
        Beer beer = Beer.builder().build();
        modelMapper.map(fields, beer);

        return beerService.alter(beer);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Beer deleteBeer(@PathVariable String id) {
        return beerService.delete(Integer.parseInt(id));
    }
}
