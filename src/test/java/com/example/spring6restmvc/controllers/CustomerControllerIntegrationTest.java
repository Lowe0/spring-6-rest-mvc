package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.model.CustomerDto;
import com.example.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest

class CustomerControllerIntegrationTest {
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void listCustomers() {
        List<CustomerDto> customers = customerController.listCustomers();
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isEqualTo(2);
    }

    @Transactional
    @Rollback
    @Test
    void listCustomers_Empty() {
        customerRepository.deleteAll();
        List<CustomerDto> customers = customerController.listCustomers();
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    void getCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDto customerDto = customerController.getCustomerById(customer.getId());
        assertThat(customerDto).isNotNull();
        assertThat(customerDto.getId()).isEqualTo(customer.getId());
    }

    @Test
    void getCustomerById_NotFound_ThrowsException() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }
}