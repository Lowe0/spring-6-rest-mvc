package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Iterable<Customer> listCustomers();

    Optional<Customer> getCustomerById(UUID id);

    Customer addCustomer(Customer toAdd);

    void updateCustomerById(UUID id, Customer toUpdate);

    void deltaCustomerById(UUID id, Customer toUpdate);

    void deleteCustomerById(UUID id);
}
