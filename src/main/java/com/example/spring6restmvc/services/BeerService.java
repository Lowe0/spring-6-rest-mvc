package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize);

    Optional<BeerDto> getBeerByUUID(UUID id);

    BeerDto addBeer(BeerDto toAdd);

    Optional<BeerDto> updateBeerById(UUID id, BeerDto toUpdate);

    Optional<BeerDto> deltaBeerById(UUID id, BeerDto toUpdate);

    Boolean deleteBeerById(UUID id);
}
