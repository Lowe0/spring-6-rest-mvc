package com.example.spring6restmvc.services;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.mappers.BeerMapper;
import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.repositories.BeerRepository;
import com.example.spring6restmvc.specifications.BeerSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDto> listBeers(String beerName) {
        List<Beer> beerList;

        List<Specification<Beer>> criteria = new ArrayList<>();

        if (!StringUtils.isEmpty(beerName)) {
            criteria.add(BeerSpecifications.beerNameLike("%" + beerName + "%"));
        }

        if (criteria.isEmpty()) {
            beerList = beerRepository.findAll();
        } else {
            Specification<Beer> spec = Specification.where(null);
            for (Specification<Beer> criterion : criteria) {
                spec = spec.and(criterion);
            }
            beerList = beerRepository.findAll(spec);
        }

        return beerList
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    private List<Beer> listBeersByName(String beerName) {
        return beerRepository.findAll(BeerSpecifications.beerNameLike("%" + beerName + "%"));
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
    public Optional<BeerDto> updateBeerById(UUID id, BeerDto toUpdate) {
        AtomicReference<Optional<BeerDto>> refUpdated = new AtomicReference<>();

        beerRepository.findById(id).ifPresentOrElse(x -> {
            x.setBeerName(toUpdate.getBeerName());
            x.setBeerStyle(toUpdate.getBeerStyle());
            x.setUpc(toUpdate.getUpc());
            x.setPrice(toUpdate.getPrice());
            refUpdated.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(x))));
        }, () -> refUpdated.set(Optional.empty()));
        return refUpdated.get();
    }

    @Override
    public Optional<BeerDto> deltaBeerById(UUID id, BeerDto toUpdate) {
        AtomicReference<Optional<BeerDto>> refUpdated = new AtomicReference<>();

        beerRepository.findById(id).ifPresentOrElse(x -> {
            beerMapper.deltaBeerFromBeerDto(toUpdate, x);
            refUpdated.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(x))));
        }, () -> refUpdated.set(Optional.empty()));
        return refUpdated.get();
    }

    @Override
    public Boolean deleteBeerById(UUID id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        } else { return false; }
    }
}
