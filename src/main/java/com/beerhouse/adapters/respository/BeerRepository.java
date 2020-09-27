package com.beerhouse.adapters.respository;

import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.ports.BeerRepositoryPort;

import java.util.List;

public class BeerRepository implements BeerRepositoryPort {

    BeerJpaRepository beerRepository;

    @Override
    public List<Beer> retrieveMany() {
        return beerRepository.findAll();
    }

    @Override
    public Beer retrieveOne(int id) {
        return beerRepository.findOne(id);
    }

    @Override
    public void create(Beer beer) {
        beerRepository.save(beer);
    }

    @Override
    public void update(Beer beer) {
        beerRepository.save(beer);
    }

    @Override
    public void alter(Beer beer) {
        beerRepository.save(beer);
    }

    @Override
    public void delete(int id) {
        beerRepository.delete(id);
    }
}
