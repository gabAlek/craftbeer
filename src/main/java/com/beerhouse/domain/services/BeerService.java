package com.beerhouse.domain.services;

import com.beerhouse.adapters.respository.BeerRepository;
import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.ports.BeerPersistencePort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerService implements BeerPersistencePort {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Beer> retrieveMany() {
        return null;
    }

    @Override
    public Beer retrieveOne(int id) {

        return null;
    }

    @Override
    public Beer create(Beer beer) {

        return null;
    }

    @Override
    public Beer update(Beer beer) {

        return null;
    }

    @Override
    public Beer alter(Beer beer) {
        return null;
    }

    @Override
    public Beer delete(int id) {

        return null;
    }
}
