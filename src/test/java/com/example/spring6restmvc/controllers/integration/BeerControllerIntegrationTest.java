package com.example.spring6restmvc.controllers.integration;

import com.example.spring6restmvc.controllers.BeerController;
import com.example.spring6restmvc.controllers.NotFoundException;
import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.mappers.BeerMapper;
import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.model.BeerStyle;
import com.example.spring6restmvc.repositories.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static com.example.spring6restmvc.controllers.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerControllerIntegrationTest {
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void listBeers() {
        var beerDtos = beerController.listBeers(null,null, null, null, null, null);
        assertThat(beerDtos).isNotNull();
        assertThat(beerDtos.getTotalElements()).isEqualTo(2410);
        assertThat(beerDtos.getContent()).isNotNull();
        assertThat(beerDtos.getContent().size()).isEqualTo(50);
    }

    @Transactional
    @Rollback
    @Test
    void listBeers_Empty() {
        beerRepository.deleteAll();
        var beerDtos = beerController.listBeers(null, null, null, null, null, null);
        assertThat(beerDtos).isNotNull();
        assertThat(beerDtos.getTotalElements()).isEqualTo(0);

    }

    @Test
    void listBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "%IPA%"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(336))
                .andExpect(jsonPath("$.content.length()").value(50));
    }

    @Test
    void listBeersByStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(571))
                .andExpect(jsonPath("$.content.length()").value(50));
    }

    @Test
    void listBeersByNameAndStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "%IPA%")
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(324))
                .andExpect(jsonPath("$.content.length()").value(50));
    }

    @Test
    void listBeersByNameAndStyle_page2() throws Exception {
        int pageSize = 33;
        int pageNumber = 1;    // I'm assuming pages are zero-indexed, so page 2 is index 1

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "%IPA%")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .queryParam("pageNum", String.valueOf(pageNumber)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(324))
                .andExpect(jsonPath("$.content.length()").value(pageSize));
    }

    @Test
    void listBeersByNameAndStyle_sortByName_page3() throws Exception {
        int pageSize = 33;
        int pageNumber = 2;    // I'm assuming pages are zero-indexed, so page 3 is index 2

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "%IPA%")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .queryParam("pageNum", String.valueOf(pageNumber))
                        .queryParam("sortBy", "beerName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(324))
                .andExpect(jsonPath("$.content.length()").value(pageSize));
    }

    @Test
    void listBeersByNameAndStyle_sortByNameDesc_page3() throws Exception {
        int pageSize = 33;
        int pageNumber = 2;    // I'm assuming pages are zero-indexed, so page 3 is index 2

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "%IPA%")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .queryParam("pageNum", String.valueOf(pageNumber))
                        .queryParam("sortDirection", "DESC")
                        .queryParam("sortBy", "beerName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(324))
                .andExpect(jsonPath("$.content.length()").value(pageSize));
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
        BeerDto toAdd = BeerDto.builder().beerName("New Beer").build();

        ResponseEntity response = beerController.addBeer(toAdd);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders()).containsKey("Location");

        String uuid = response.getHeaders().getLocation().getPath().split("/")[4];
        Beer addedBeer = beerRepository.findById(UUID.fromString(uuid)).get();
        assertThat(addedBeer).isNotNull();
        assertThat(addedBeer.getBeerName()).isEqualTo(toAdd.getBeerName());
    }

    @Transactional
    @Rollback
    @Test
    void updateBeer() {
        Beer beerBeforeUpdate = beerRepository.findAll().get(0);
        BeerDto toUpdate = beerMapper.beerToBeerDto(beerBeforeUpdate);
        toUpdate.setId(null);
        toUpdate.setVersion(null);
        toUpdate.setBeerName("Updated Beer");

        ResponseEntity response = beerController.updateBeerById(beerBeforeUpdate.getId(), toUpdate);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getHeaders()).containsKey("Location");
        String uuid = response.getHeaders().getLocation().getPath().split("/")[4];

        Beer beerAfterUpdate = beerRepository.findById(UUID.fromString(uuid)).get();
        assertThat(beerAfterUpdate).isNotNull();
        assertThat(beerAfterUpdate.getId()).isEqualTo(beerBeforeUpdate.getId());
        assertThat(beerAfterUpdate.getBeerName()).isEqualTo(toUpdate.getBeerName());
        assertThat(beerAfterUpdate.getBeerStyle()).isEqualTo(toUpdate.getBeerStyle());
        assertThat(beerAfterUpdate.getPrice()).isEqualTo(toUpdate.getPrice());
        assertThat(beerAfterUpdate.getUpc()).isEqualTo(toUpdate.getUpc());
    }

    @Transactional
    @Rollback
    @Test
    void updateBeer_NotFound_ThrowsException() {
        assertThrows(NotFoundException.class, () -> beerController.updateBeerById(UUID.randomUUID(), BeerDto.builder().build()));
    }

    @Transactional
    @Rollback
    @Test
    void deltaBeer() {
        Beer beerBeforeUpdate = beerRepository.findAll().get(0);
        BeerDto toUpdate = BeerDto.builder().beerName("Updated Beer").build();

        ResponseEntity response = beerController.deltaBeerById(beerBeforeUpdate.getId(), toUpdate);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getHeaders()).containsKey("Location");
        String uuid = response.getHeaders().getLocation().getPath().split("/")[4];

        Beer beerAfterUpdate = beerRepository.findById(UUID.fromString(uuid)).get();
        assertThat(beerAfterUpdate).isNotNull();
        assertThat(beerAfterUpdate.getId()).isEqualTo(beerBeforeUpdate.getId());
        assertThat(beerAfterUpdate.getBeerName()).isEqualTo(toUpdate.getBeerName());
        assertThat(beerAfterUpdate.getBeerStyle()).isEqualTo(beerBeforeUpdate.getBeerStyle());
        assertThat(beerAfterUpdate.getPrice()).isEqualTo(beerBeforeUpdate.getPrice());
        assertThat(beerAfterUpdate.getUpc()).isEqualTo(beerBeforeUpdate.getUpc());
    }

    @Transactional
    @Rollback
    @Test
    void deltaBeer_NotFound_ThrowsException() {
        assertThrows(NotFoundException.class, () -> beerController.deltaBeerById(UUID.randomUUID(), BeerDto.builder().build()));
    }

    @Test
    void deltaBeer_nameTooLong_ThrowsException() throws Exception{
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

        var result = mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andReturn();

        var errorMaps = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);
        assertThat(errorMaps).anyMatch(x -> {
            var errorMap = (LinkedHashMap<String, String>) x;
            return errorMap.containsKey("beerName") && errorMap.get("beerName").equals("size must be between 0 and 50");
        });
    }

    @Transactional
    @Rollback
    @Test
    void deleteBeer() {
        Beer beerBeforeDelete = beerRepository.findAll().get(0);
        beerController.deleteBeerById(beerBeforeDelete.getId());

        assertThat(beerRepository.existsById(beerBeforeDelete.getId())).isFalse();
    }

    @Transactional
    @Rollback
    @Test
    void deleteBeer_NotFound_ThrowsException() {
        assertThrows(NotFoundException.class, () -> beerController.deleteBeerById(UUID.randomUUID()));
    }
}