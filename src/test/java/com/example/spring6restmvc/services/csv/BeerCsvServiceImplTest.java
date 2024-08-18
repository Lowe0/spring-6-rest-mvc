package com.example.spring6restmvc.services.csv;

import com.example.spring6restmvc.model.csv.BeerCsv;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeerCsvServiceImplTest {

    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void parseCsv() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<BeerCsv> beers = beerCsvService.parseCsv(file);

        assertNotNull(beers);
        assertThat(beers).isNotEmpty();
        assertThat(beers.size()).isEqualTo(2410);
    }
}