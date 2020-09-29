package com.beerhouse.domain.model.exception;

public class NoBeersFoundException extends RuntimeException {
    public NoBeersFoundException() {
        super("No beers were found");
    }
}
