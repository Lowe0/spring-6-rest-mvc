package com.example.spring6restmvc.services;

import com.example.spring6restmvc.mappers.BeerMapper;
import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDto> listBeers() {
        return beerRepository
                .findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDto> getBeerByUUID(UUID id) {
        return Optional.ofNullable(beerRepository.findById(id).orElse(null)).map(beerMapper::beerToBeerDto);
    }

    @Override
    public BeerDto addBeer(BeerDto toAdd) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(toAdd)));
    }

    @Override
    public void updateBeerById(UUID id, BeerDto toUpdate) {
        beerRepository.findById(id).ifPresent(x -> {
            x.setBeerName(toUpdate.getBeerName());
            x.setBeerStyle(toUpdate.getBeerStyle());
            x.setUpc(toUpdate.getUpc());
            x.setPrice(toUpdate.getPrice());
            beerRepository.save(x);
        });
    }

    @Override
    public void deltaBeerById(UUID id, BeerDto toUpdate) {

    }

    @Override
    public void deleteBeerById(UUID id) {

    }
}
