package com.beerhouse.adapters.controllers;

import com.beerhouse.domain.model.exception.BeerAlreadyExistsException;
import com.beerhouse.domain.model.exception.BeerNotFoundException;
import com.beerhouse.domain.model.exception.NoBeersFoundException;
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
    @ExceptionHandler(NoBeersFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String noBeersWereFound(NoBeersFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(BeerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String beerAlreadyExists(BeerAlreadyExistsException ex) {
        return ex.getMessage();
    }
}
