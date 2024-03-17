package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerByUUID(UUID id);

    Iterable<Beer> listBeers();

    Beer addNewBeer(Beer toAdd);

    Beer updateBeerById(UUID id, Beer toUpdate);
}
