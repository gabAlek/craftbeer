package com.beerhouse.domain.model.exception;

public class BeerAlreadyExistsException extends RuntimeException {
    public BeerAlreadyExistsException(String name) {
        super("Beer with name " + name + " already exists");
    }
}
