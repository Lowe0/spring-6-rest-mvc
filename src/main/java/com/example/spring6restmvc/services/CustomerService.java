package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDto> listCustomers();

    Optional<CustomerDto> getCustomerById(UUID id);

    CustomerDto addCustomer(CustomerDto toAdd);

    Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto toUpdate);

    Optional<CustomerDto> deltaCustomerById(UUID id, CustomerDto toUpdate);

    Boolean deleteCustomerById(UUID id);
}
