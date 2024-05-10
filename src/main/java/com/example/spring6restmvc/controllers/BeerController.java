package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.model.Beer;
import com.example.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Beer> listBeers() {
        return beerService.listBeers();
    }

    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID id) {
        log.debug("Get Beer by ID - controller");
        return beerService.getBeerByUUID(id);
    }

    @PostMapping
    public ResponseEntity addBeer(@RequestBody Beer toAdd) {
        Beer beerSaved = beerService.addBeer(toAdd);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + beerSaved.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer toUpdate) {
        beerService.updateBeerById(id, toUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + id);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/{beerId}")
    public ResponseEntity deltaBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer toUpdate) {
        beerService.deltaBeerById(id, toUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + id);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{beerId}")
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID id) {
        beerService.deleteBeerById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
