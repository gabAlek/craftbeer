package com.beerhouse.adapters.respository;

import com.beerhouse.domain.model.Beer;
import com.beerhouse.domain.ports.BeerRepositoryPort;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeerRepository implements BeerRepositoryPort {

    @Autowired
    BeerJpaRepository beerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Beer> retrieveMany() {
        List<BeerEntity> results = beerRepository.findAll();
        if(results.isEmpty()){
            return null;
        }

        return modelMapper.map(results,new TypeToken<List<Beer>>(){}.getType());
    }

    @Override
    public Beer retrieveOne(int id) {
        Optional<BeerEntity> results = beerRepository.findById(id);
        if(!results.isPresent()){
            return null;
        }
        return modelMapper.map(results.get(),Beer.class);
    }

    @Override
    public Beer create(Beer beer) {
        BeerEntity beerRecord = modelMapper.map(beer, BeerEntity.class);
        BeerEntity results = beerRepository.save(beerRecord);

        return modelMapper.map(results,Beer.class);
    }

    @Override
    public Beer update(Beer replacementBeer) {
        BeerEntity replacementRecord = modelMapper.map(replacementBeer,BeerEntity.class);

        BeerEntity results = beerRepository.save(replacementRecord);

        return modelMapper.map(results,Beer.class);
    }

    @Override
    public Beer alter(Beer replacementBeer) {
        BeerEntity replacementRecord = modelMapper.map(replacementBeer,BeerEntity.class);

        BeerEntity results = beerRepository.save(replacementRecord);

        return modelMapper.map(results,Beer.class);
    }

    @Override
    public Long delete(int id) {

        return beerRepository.deleteById(id);
    }

    @Override
    public boolean nameExists(String name) {
        return beerRepository.existsBeerEntityByName(name);
    }
}
