package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.BeerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDto> listBeers(String beerName);

    Optional<BeerDto> getBeerByUUID(UUID id);

    BeerDto addBeer(BeerDto toAdd);

    Optional<BeerDto> updateBeerById(UUID id, BeerDto toUpdate);

    Optional<BeerDto> deltaBeerById(UUID id, BeerDto toUpdate);

    Boolean deleteBeerById(UUID id);
}
