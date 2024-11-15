package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.entities.BeerOrder;
import com.example.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerOrderRepositoryTest {
    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testBeerOrders() {
        var testOrder = BeerOrder.builder()
                .customerRef("Test Order")
                .customer(testCustomer)
                .build();

        var saved = beerOrderRepository.saveAndFlush(testOrder);
    }
}