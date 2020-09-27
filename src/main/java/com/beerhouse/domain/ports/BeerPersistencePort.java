package com.beerhouse.domain.ports;

import com.beerhouse.domain.model.Beer;

import java.util.List;

public interface BeerPersistencePort {

    List<Beer> retrieveMany();

    Beer retrieveOne(int id);

    Beer create(Beer beer);
    Beer update(Beer beer);
    Beer alter(Beer beer);

    Beer delete(int id);
}
