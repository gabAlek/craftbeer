package com.beerhouse.adapters.controllers;

import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.ports.BeerPersistencePort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/beers", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @ResponseBody
    public Beer retrieveBeer(@PathVariable String id) {
        int parsedId = Integer.parseInt(id);
        return beerService.retrieveOne(parsedId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Beer createBeer(@Valid @RequestBody BeerRequest body) {
        return beerService.create(modelMapper.map(body, Beer.class));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Beer updateBeer(@PathVariable String id,
                           @Valid @RequestBody BeerRequest body) {

        int requestedId = Integer.parseInt(id);

        if(body.getId() != requestedId)
            throw new IllegalArgumentException(
                    "The id informed in the path is different from the one of the resource." +
                            " Path id: " + id +
                            ", resource id:" + body.getId());

        Beer replacementBeer = modelMapper.map(body,Beer.class);

        return beerService.update(requestedId,replacementBeer);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Beer alterBeer(@PathVariable String id,
                          @RequestBody LinkedHashMap<String,Object> fields) {
        return beerService.alter(Integer.parseInt(id),fields);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteBeer(@PathVariable String id) {

        Long serviceResult = beerService.delete(Integer.parseInt(id));
        return new ResponseEntity<>(serviceResult + " row(s) deleted", HttpStatus.NO_CONTENT);
    }
}
