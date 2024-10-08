package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.model.BeerStyle;
import com.example.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
    private final BeerService beerService;
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    @GetMapping(value = BEER_PATH)
    public Page<BeerDto> listBeers(
            @RequestParam(name = "beerName", required = false) String beerName,
            @RequestParam(name = "beerStyle", required = false) BeerStyle beerStyle,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @RequestParam(name = "sortBy", required = false) String sortBy) {
        return beerService.listBeers(beerName, beerStyle, pageNumber, pageSize, null, sortBy);
    }

    @GetMapping(value = BEER_PATH_ID)
    public BeerDto getBeerById(@PathVariable("beerId") UUID id) {
        log.debug("Get Beer by ID - controller");
        return beerService.getBeerByUUID(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(value = BEER_PATH)
    public ResponseEntity addBeer(@Validated @RequestBody BeerDto toAdd) {
        BeerDto beerSaved = beerService.addBeer(toAdd);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + beerSaved.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = BEER_PATH_ID)
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID id, @Validated @RequestBody BeerDto toUpdate) {
        beerService.updateBeerById(id, toUpdate).ifPresentOrElse(x -> {
        }, () -> {
            throw new NotFoundException();
        });

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + id);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = BEER_PATH_ID)
    public ResponseEntity deltaBeerById(@PathVariable("beerId") UUID id, @RequestBody BeerDto toUpdate) {
        beerService.deltaBeerById(id, toUpdate).ifPresentOrElse(x -> {
        }, () -> {
            throw new NotFoundException();
        });

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + id);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = BEER_PATH_ID)
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID id) {
        if (!beerService.deleteBeerById(id)) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
