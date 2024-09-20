package com.example.spring6restmvc.services;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.mappers.BeerMapper;
import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.model.BeerStyle;
import com.example.spring6restmvc.repositories.BeerRepository;
import com.example.spring6restmvc.specifications.BeerSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final String DEFAULT_SORT_FIELD = "id";

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize, String sortDirection, String sortBy) {
        Page<Beer> beerPage;

        List<Specification<Beer>> criteria = new ArrayList<>();

        if (!StringUtils.isEmpty(beerName)) {
            criteria.add(BeerSpecifications.beerNameLike("%" + beerName + "%"));
        }
        if (beerStyle != null) {
            criteria.add(BeerSpecifications.beerStyleEquals(beerStyle));
        }

        PageRequest pageRequest = getPageRequest(pageNumber, pageSize, sortDirection, sortBy);

        if (criteria.isEmpty()) {
            beerPage = beerRepository.findAll(pageRequest);
        } else {
            Specification<Beer> spec = Specification.where(null);
            for (Specification<Beer> criterion : criteria) {
                spec = spec.and(criterion);
            }
            beerPage = beerRepository.findAll(spec, pageRequest);
        }

        return beerPage
                .map(beerMapper::beerToBeerDto);
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

    private static PageRequest getPageRequest(Integer pageNumber, Integer pageSize, String sortDirection, String sortProperty) {
        int coalescedPageNumber = pageNumber == null ? DEFAULT_PAGE_NUMBER : pageNumber;
        int coalescedPageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        Sort.Direction coalescedSortDirection =  isValidSortDirection(sortDirection) ? Sort.Direction.fromString(sortDirection) : Sort.DEFAULT_DIRECTION;
        String coalescedSortProperty = isValidProperty(sortProperty) ? sortProperty : DEFAULT_SORT_FIELD;

        return PageRequest.of(coalescedPageNumber, coalescedPageSize, Sort.by(coalescedSortDirection, coalescedSortProperty));
    }

    private static boolean isValidSortDirection(String sortDirection) {
        var validDirections = List.of("asc", "desc");
        return StringUtils.hasText(sortDirection) && validDirections.contains(sortDirection.toLowerCase(Locale.ROOT));
    }

    private static boolean isValidProperty(String sortProperty) {
        var validProperties = List.of("id", "beerName", "beerStyle", "price", "upc");
        return StringUtils.hasText(sortProperty) && validProperties.contains(sortProperty);
    }
}
