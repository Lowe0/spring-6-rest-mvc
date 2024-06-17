package com.example.spring6restmvc.bootstrap;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.model.BeerStyle;
import com.example.spring6restmvc.repositories.BeerRepository;
import com.example.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        populateBeer();
        populateCustomer();
    }

    private void populateBeer() {
        if (beerRepository.count() == 0) {
            Beer sunlight = Beer.builder()
                    .beerName("Sunlight")
                    .beerStyle(BeerStyle.CREAM_ALE)
                    .upc("01 11111 00000 10")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(99)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            Beer osiris = Beer.builder()
                    .beerName("Osiris")
                    .beerStyle(BeerStyle.IPA)
                    .upc("01 11111 00002 11")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(99)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            Beer weeMac = Beer.builder()
                    .beerName("Wee Mac")
                    .beerStyle(BeerStyle.SCOTTISH_ALE)
                    .upc("01 11111 00003 20")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(99)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            log.debug("Initializing beer");
            beerRepository.save(sunlight);
            beerRepository.save(osiris);
            beerRepository.save(weeMac);
            log.debug("Beer initialized");
        }
    }

    private void populateCustomer() {
        if (customerRepository.count() == 0) {
            Customer kevin = Customer.builder()
                    .customerName("Kevin Lowe")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now()).build();

            Customer cassi = Customer.builder()
                    .customerName("Cassi Lowe")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now()).build();

            log.debug("Initializing customer");
            customerRepository.save(kevin);
            customerRepository.save(cassi);
            log.debug("Customer initialized");
        }
    }
}
