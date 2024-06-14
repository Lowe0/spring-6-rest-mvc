package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.services.BeerService;
import com.example.spring6restmvc.services.BeerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.spring6restmvc.controllers.BeerController.BEER_PATH;
import static com.example.spring6restmvc.controllers.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDto> beerArgumentCaptor;

    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setup() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void getAllBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get(BEER_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));

        verify(beerService).listBeers();
    }

    @Test
    void getBeerById() throws Exception {
        BeerDto testBeer = beerServiceImpl.listBeers().iterator().next();

        given(beerService.getBeerByUUID(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BEER_PATH_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testBeer.getId().toString()))
                .andExpect(jsonPath("$.beerName").value(testBeer.getBeerName()));

        verify(beerService).getBeerByUUID(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testBeer.getId());
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerByUUID(any(UUID.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void addBeer() throws Exception {
        BeerDto beer = beerServiceImpl.listBeers().iterator().next();
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.addBeer(any(BeerDto.class))).willReturn(beerServiceImpl.listBeers().iterator().next());

        mockMvc.perform(post(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        verify(beerService).addBeer(beerArgumentCaptor.capture());
        assertThat(beerArgumentCaptor.getValue()).isEqualTo(beer);
    }

    @Test
    void updateBeer() throws Exception {
        BeerDto beer = beerServiceImpl.listBeers().iterator().next();

        mockMvc.perform(put(BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beer.getId());
    }

    @Test
    void deleteBeer() throws Exception {
        BeerDto beer = beerServiceImpl.listBeers().iterator().next();

        mockMvc.perform(delete(BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beer.getId());
    }

    @Test
    void deltaBeer() throws Exception {
        BeerDto beer = beerServiceImpl.listBeers().iterator().next();

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).deltaBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beer.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beerMap.get("beerName"));
    }
}