package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDto beerDto = beerController.getBeerById(beer.getId());
        assertThat(beerDto).isNotNull();
        assertThat(beerDto.getId()).isEqualTo(beer.getId());
    }

    @Test
    void getBeerById_NotFound_ThrowsException() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void addBeer() {
        BeerDto toAdd = BeerDto.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity response = beerController.addBeer(toAdd);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders()).containsKey("Location");

        String uuid = response.getHeaders().getLocation().getPath().split("/")[4];
        Beer addedBeer = beerRepository.findById(UUID.fromString(uuid)).get();
        assertThat(addedBeer).isNotNull();
        assertThat(addedBeer.getBeerName()).isEqualTo(toAdd.getBeerName());
    }
}