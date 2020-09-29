package com.beerhouse.domain.model.exception;

public class BeerNotFoundException extends RuntimeException {
    public BeerNotFoundException(Integer id) {
        super("Could not find beer with id " + id);
    }
}
