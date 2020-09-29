package com.beerhouse.domain.services;

import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.model.exception.BeerAlreadyExistsException;
import com.beerhouse.domain.model.exception.BeerNotFoundException;
import com.beerhouse.domain.model.exception.NoBeersFoundException;
import com.beerhouse.domain.ports.BeerPersistencePort;
import com.beerhouse.domain.ports.BeerRepositoryPort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class BeerService implements BeerPersistencePort {

    @Autowired
    BeerRepositoryPort beerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Beer> retrieveMany() {
        List<Beer> results = beerRepository.retrieveMany();

        if(results == null) {
            throw new NoBeersFoundException();
        }

        return results;
    }

    @Override
    public Beer retrieveOne(int id) {
        Beer result = beerRepository.retrieveOne(id);

        if(result == null) {
            throw new BeerNotFoundException(id);
        }

        return result;
    }

    @Override
    public Beer create(Beer beer) {

        String newBeerName = beer.getName();
        boolean nameAlreadyExists = beerRepository.nameExists(newBeerName);

        if(nameAlreadyExists) throw new BeerAlreadyExistsException(newBeerName);

        Beer insertedBeer = beerRepository.create(beer);
        return insertedBeer;
    }

    @Override
    public Beer update(int id,Beer replacementBeer) {
        Beer existingBeer = beerRepository.retrieveOne(id);

        if(existingBeer == null) {
            throw new BeerNotFoundException(id);
        }

        String existingBeerName = existingBeer.getName();
        String replacementBeerName = replacementBeer.getName();

        if(existingBeerName != replacementBeerName) {
            boolean nameAlreadyExists = beerRepository.nameExists(replacementBeerName);
            if(nameAlreadyExists) throw new BeerAlreadyExistsException(replacementBeerName);
        }

        modelMapper.map(replacementBeer,existingBeer);

        Beer replacedBeer = beerRepository.update(existingBeer);
        return replacedBeer;
    }

    @Override
    public Beer alter(int id , LinkedHashMap<String,Object> fields) {
        Beer existingBeer = beerRepository.retrieveOne(id);

        if(existingBeer == null) {
            throw new BeerNotFoundException(id);
        }

        String existingBeerName = existingBeer.getName();

        if(fields.containsKey("name") &&
                (!existingBeerName.equals(fields.get("name")))) {

            String replacementBeerName = (String) fields.get("name");

            boolean nameAlreadyExists = beerRepository.nameExists(replacementBeerName);
            if(nameAlreadyExists) throw new BeerAlreadyExistsException(replacementBeerName);
        }

        modelMapper.map(fields,existingBeer);

        Beer replacedBeer = beerRepository.alter(existingBeer);
        return replacedBeer;
    }

    @Override
    public Long delete(int id) {
        Long numberOfDeletedRows = beerRepository.delete(id);

        if(numberOfDeletedRows == null || numberOfDeletedRows != 1) {
            throw new BeerNotFoundException(id);
        }

        return numberOfDeletedRows;
    }
}
