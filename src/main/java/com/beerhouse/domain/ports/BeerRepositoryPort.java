package com.beerhouse.domain.ports;

import com.beerhouse.domain.model.Beer;

import java.util.List;

public interface BeerRepositoryPort {
    List<Beer> retrieveMany();

    Beer retrieveOne(int id);

    void create(Beer beer);

    void update(Beer beer);

    void alter(Beer beer);

    void delete(int id);
}
