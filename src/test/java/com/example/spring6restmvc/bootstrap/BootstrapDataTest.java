package com.example.spring6restmvc.bootstrap;

import com.example.spring6restmvc.repositories.BeerRepository;
import com.example.spring6restmvc.repositories.CustomerRepository;
import com.example.spring6restmvc.services.csv.BeerCsvService;
import com.example.spring6restmvc.services.csv.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BeerCsvService beerCsvService;

    BootstrapData bootstrapData;
    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
    }

    @Test
    void run() throws Exception {
        bootstrapData.run();
        assertThat(beerRepository.count()).isEqualTo(2410);
        assertThat(customerRepository.count()).isEqualTo(2);
    }
}