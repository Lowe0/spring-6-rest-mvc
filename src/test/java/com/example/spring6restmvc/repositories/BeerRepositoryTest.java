package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void addBeer() {
        Beer beer = beerRepository.save(Beer.builder().beerName("Test Beer").build());

        assertThat(beer).isNotNull();
        assertThat(beer.getId()).isNotNull();
        assertThat(beer.getBeerName()).isEqualTo("Test Beer");
    }

}