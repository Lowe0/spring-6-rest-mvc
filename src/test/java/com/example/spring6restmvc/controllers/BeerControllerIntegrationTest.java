package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerControllerIntegrationTest {
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Test
    void listBeers() {
        List<BeerDto> beerDtos = beerController.listBeers();
        assertThat(beerDtos).isNotNull();
        assertThat(beerDtos.size()).isEqualTo(3);
    }

    @Transactional
    @Rollback
    @Test
    void listBeers_Empty() {
        beerRepository.deleteAll();
        List<BeerDto> beerDtos = beerController.listBeers();
        assertThat(beerDtos).isNotNull();
        assertThat(beerDtos.size()).isEqualTo(0);

    }
}