package com.beerhouse.domain.ports;

import com.beerhouse.domain.model.Beer;

import java.util.List;

public interface BeerRepositoryPort {
    List<Beer> retrieveMany();

    Beer retrieveOne(int id);

    Beer create(Beer beer);

    Beer update(Beer beer);

    Beer alter(Beer beer);

    Long delete(int id);

    boolean nameExists(String name);
}
