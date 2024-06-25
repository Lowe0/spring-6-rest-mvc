package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.mappers.CustomerMapper;
import com.example.spring6restmvc.model.CustomerDto;
import com.example.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
    @Autowired
    CustomerMapper customerMapper;

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

    @Transactional
    @Rollback
    @Test
    void addCustomer() {
        CustomerDto toAdd = CustomerDto.builder().customerName("New Customer").build();

        var response = customerController.addCustomer(toAdd);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().containsKey("Location")).isTrue();

        var location = response.getHeaders().getLocation().getPath().split("/")[4];
        var addedCustomer = customerRepository.findById(UUID.fromString(location));
        assertThat(addedCustomer.isPresent()).isTrue();
        assertThat(addedCustomer.get().getCustomerName()).isEqualTo(toAdd.getCustomerName());
    }

    @Transactional
    @Rollback
    @Test
    void updateCustomer() {
        var customer = customerRepository.findAll().get(0);
        var toUpdate = customerMapper.customerToCustomerDto(customer);
        toUpdate.setId(null);
        toUpdate.setVersion(null);
        toUpdate.setCustomerName("Updated Customer");

        var response = customerController.updateCustomerById(customer.getId(), toUpdate);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getHeaders().containsKey("Location")).isTrue();

        var location = response.getHeaders().getLocation().getPath().split("/")[4];
        var updatedCustomer = customerRepository.findById(UUID.fromString(location));
        assertThat(updatedCustomer.isPresent()).isTrue();
        assertThat(updatedCustomer.get().getCustomerName()).isEqualTo(toUpdate.getCustomerName());
    }

    @Transactional
    @Rollback
    @Test
    void updateCustomer_NotFound_ThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDto.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void deltaCustomer() {
        var customerBeforeUpdate = customerRepository.findAll().get(0);
        var toUpdate = CustomerDto.builder().customerName("Updated Customer").build();

        var response = customerController.deltaCustomerById(customerBeforeUpdate.getId(), toUpdate);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getHeaders().containsKey("Location")).isTrue();

        var location = response.getHeaders().getLocation().getPath().split("/")[4];
        var customerAfterUpdate = customerRepository.findById(UUID.fromString(location));
        assertThat(customerAfterUpdate.isPresent()).isTrue();
        assertThat(customerAfterUpdate.get().getCustomerName()).isEqualTo(toUpdate.getCustomerName());
    }

    @Transactional
    @Rollback
    @Test
    void deltaCustomer_NotFound_ThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deltaCustomerById(UUID.randomUUID(), CustomerDto.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void deleteCustomer() {
        var toDelete = customerRepository.findAll().get(0);

        var response = customerController.deleteCustomerById(toDelete.getId());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(customerRepository.findById(toDelete.getId()).isPresent()).isFalse();
    }

    @Transactional
    @Rollback
    @Test
    void deleteCustomer_NotFound_ThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }
}