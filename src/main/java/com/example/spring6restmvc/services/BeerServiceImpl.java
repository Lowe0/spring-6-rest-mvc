package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, BeerDto> beerMap;

    public BeerServiceImpl() {
        populateMap();
    }

    private void populateMap() {
        this.beerMap = new HashMap<>();

        BeerDto sunlight = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunlight")
                .beerStyle(BeerStyle.LAGER)
                .upc("01 11111 00000 10")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(99)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDto osiris = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Osiris")
                .beerStyle(BeerStyle.IPA)
                .upc("01 11111 00002 11")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(99)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDto weeMac = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Wee Mac")
                .beerStyle(BeerStyle.ALE)
                .upc("01 11111 00003 20")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(99)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerMap.put(sunlight.getId(), sunlight);
        beerMap.put(osiris.getId(), osiris);
        beerMap.put(weeMac.getId(), weeMac);
    }

    @Override
    public List<BeerDto> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDto> getBeerByUUID(UUID id) {
        log.debug("Get Beer by ID - service");
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDto addBeer(BeerDto toAdd) {
        BeerDto savedBeer = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .beerName(toAdd.getBeerName())
                .beerStyle(toAdd.getBeerStyle())
                .quantityOnHand(toAdd.getQuantityOnHand())
                .upc(toAdd.getUpc())
                .price(toAdd.getPrice())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Optional<BeerDto> updateBeerById(UUID id, BeerDto toUpdate) {
        BeerDto savedBeer = beerMap.get(id);
        savedBeer.setBeerName(toUpdate.getBeerName());
        savedBeer.setBeerStyle(toUpdate.getBeerStyle());
        savedBeer.setQuantityOnHand(toUpdate.getQuantityOnHand());
        savedBeer.setUpc(toUpdate.getUpc());
        savedBeer.setPrice(toUpdate.getPrice());
        savedBeer.setVersion(savedBeer.getVersion() + 1);
        savedBeer.setUpdatedDate(LocalDateTime.now());

        beerMap.put(id, savedBeer);
        return Optional.of(savedBeer);
    }

    @Override
    public Optional<BeerDto> deltaBeerById(UUID id, BeerDto toUpdate) {
        BeerDto savedBeer = beerMap.get(id);
        if (StringUtils.hasText(toUpdate.getBeerName())) {
            savedBeer.setBeerName(toUpdate.getBeerName());
        }
        if (toUpdate.getBeerStyle() != null) {
            savedBeer.setBeerStyle(toUpdate.getBeerStyle());
        }
        if(toUpdate.getQuantityOnHand() != null) {
            savedBeer.setQuantityOnHand(toUpdate.getQuantityOnHand());
        }
        if(StringUtils.hasText(toUpdate.getUpc())) {
            savedBeer.setUpc(toUpdate.getUpc());
        }
        if(toUpdate.getPrice() != null) {
            savedBeer.setPrice(toUpdate.getPrice());
        }
        savedBeer.setVersion(savedBeer.getVersion() + 1);
        savedBeer.setUpdatedDate(LocalDateTime.now());

        beerMap.put(id, savedBeer);
        return null;
    }

    @Override
    public Boolean deleteBeerById(UUID id) {
        beerMap.remove(id);
        return null;
    }
}
