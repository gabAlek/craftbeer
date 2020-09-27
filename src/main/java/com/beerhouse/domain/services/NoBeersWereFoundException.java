package com.beerhouse.domain.services;

public class NoBeersWereFoundException extends RuntimeException {
    public NoBeersWereFoundException() {
        super("Could not return any beers");
    }
}
