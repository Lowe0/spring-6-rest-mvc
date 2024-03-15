package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public Beer getBeerByUUID(UUID id) {
        log.debug("Get Beer by ID - service");
        return Beer.builder()
                .id(id)
                .version(1)
                .beerName("Sunlight")
                .beerStyle(BeerStyle.CREAM_ALE)
                .upc("01 11111 00000 10")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(99)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }
}
