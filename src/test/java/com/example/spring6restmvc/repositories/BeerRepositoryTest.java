package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void addBeer() {
        Beer beer = beerRepository.save(
                Beer.builder()
                    .beerName("Test Beer")
                    .beerStyle(BeerStyle.IPA)
                    .upc("0 99999 11111 1")
                    .price(BigDecimal.valueOf(14.49))
                    .build());

        beerRepository.flush();

        assertThat(beer).isNotNull();
        assertThat(beer.getId()).isNotNull();
        assertThat(beer.getBeerName()).isEqualTo("Test Beer");
    }

    @Test
    void addBeerNotValidBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {beerRepository.save(
                Beer.builder()
                        .beerName("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                        .beerStyle(BeerStyle.IPA)
                        .upc("0 99999 11111 1")
                        .price(BigDecimal.valueOf(14.49))
                        .build());

        beerRepository.flush();});
    }
}