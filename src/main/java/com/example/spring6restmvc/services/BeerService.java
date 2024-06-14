package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.BeerDto;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Optional<BeerDto> getBeerByUUID(UUID id);

    Iterable<BeerDto> listBeers();

    BeerDto addBeer(BeerDto toAdd);

    void updateBeerById(UUID id, BeerDto toUpdate);

    void deltaBeerById(UUID id, BeerDto toUpdate);

    void deleteBeerById(UUID id);
}
