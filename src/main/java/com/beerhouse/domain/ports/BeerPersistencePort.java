package com.beerhouse.domain.ports;

import com.beerhouse.domain.model.Beer;

import java.util.LinkedHashMap;
import java.util.List;

public interface BeerPersistencePort {

    List<Beer> retrieveMany();

    Beer retrieveOne(int id);

    Beer create(Beer beer);
    Beer update(int id,Beer beer);
    Beer alter(int id, LinkedHashMap<String,Object> fields);
    Long delete(int id);
}
