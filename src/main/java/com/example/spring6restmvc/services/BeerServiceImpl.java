package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        populateMap();
    }

    private void populateMap() {
        this.beerMap = new HashMap<>();

        Beer sunlight = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunlight")
                .beerStyle(BeerStyle.CREAM_ALE)
                .upc("01 11111 00000 10")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(99)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer osiris = Beer.builder()
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

        Beer weeMac = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Wee Mac")
                .beerStyle(BeerStyle.SCOTTISH_ALE)
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
    public Beer getBeerByUUID(UUID id) {
        log.debug("Get Beer by ID - service");
        return beerMap.get(id);
    }

    @Override
    public Iterable<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }
}
