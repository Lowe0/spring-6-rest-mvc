package com.example.spring6restmvc.services;

import com.example.spring6restmvc.mappers.BeerMapper;
import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Optional<BeerDto> getBeerByUUID(UUID id) {
        return Optional.empty();
    }

    @Override
    public Iterable<BeerDto> listBeers() {
        return null;
    }

    @Override
    public BeerDto addBeer(BeerDto toAdd) {
        return null;
    }

    @Override
    public void updateBeerById(UUID id, BeerDto toUpdate) {

    }

    @Override
    public void deltaBeerById(UUID id, BeerDto toUpdate) {

    }

    @Override
    public void deleteBeerById(UUID id) {

    }
}
