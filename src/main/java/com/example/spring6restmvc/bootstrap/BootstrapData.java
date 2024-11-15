package com.example.spring6restmvc.bootstrap;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.model.BeerStyle;
import com.example.spring6restmvc.model.csv.BeerCsv;
import com.example.spring6restmvc.repositories.BeerRepository;
import com.example.spring6restmvc.repositories.CustomerRepository;
import com.example.spring6restmvc.services.csv.BeerCsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private static final long MIN_BEERS_IN_INITIALIZED_DATABASE = 10;
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvService beerCsvService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerCsvData();
        populateCustomer();
    }

    private void loadBeerCsvData() throws FileNotFoundException {
        if (beerRepository.count() < MIN_BEERS_IN_INITIALIZED_DATABASE) {
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
            List<BeerCsv> beerRecords = beerCsvService.parseCsv(file);

            beerRecords.forEach(beerRecord -> {
                // quick mapper
                BeerStyle style;
                String recordStyle = beerRecord.getStyle();
                if (recordStyle.contains("IPA")) { style = BeerStyle.IPA; }
                else if (recordStyle.contains("India Pale Ale")) {style = BeerStyle.IPA;}
                else if (recordStyle.contains("Pale Ale")) {style = BeerStyle.PALE_ALE;}
                else if (recordStyle.contains("Porter")) {style = BeerStyle.PORTER;}
                else if (recordStyle.contains("Stout")) {style = BeerStyle.STOUT;}
                else if (recordStyle.contains("Saison")) {style = BeerStyle.SAISON;}
                else if (recordStyle.contains("Ale")) {style = BeerStyle.ALE;}
                else if (recordStyle.contains("Lager")) {style = BeerStyle.LAGER;}
                else {style = BeerStyle.UNKNOWN;}

                beerRepository.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerRecord.getBeer(), 50))
                                .beerStyle(style)
                                .price(BigDecimal.TEN)
                                .quantityOnHand(1)
                                .upc("01 12345 67890 10")
                                .build()
                        );
            });
        }
    }

    private void populateCustomer() {
        if (customerRepository.count() == 0) {
            Customer kevin = Customer.builder()
                    .customerName("Kevin Lowe")
                    .createdDate(Instant.now())
                    .lastModifiedDate(Instant.now()).build();

            Customer cassi = Customer.builder()
                    .customerName("Cassi Lowe")
                    .createdDate(Instant.now())
                    .lastModifiedDate(Instant.now()).build();

            log.debug("Initializing customer");
            customerRepository.save(kevin);
            customerRepository.save(cassi);
            log.debug("Customer initialized");
        }
    }
}
