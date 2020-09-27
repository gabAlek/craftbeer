package com.beerhouse.adapters.controllers;

import com.beerhouse.domain.services.BeerNotFoundException;
import com.beerhouse.domain.services.NoBeersWereFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class BeerControllerAdvice {
    @ResponseBody
    @ExceptionHandler(BeerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String beerNotFoundHandler(BeerNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(BeerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String noBeersWereFound(NoBeersWereFoundException ex) {
        return ex.getMessage();
    }
}
