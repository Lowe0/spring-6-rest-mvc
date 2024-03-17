package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.Customer;

import java.util.UUID;

public interface CustomerService {
    Iterable<Customer> listCustomers();

    Customer getCustomerById(UUID id);

    Customer addCustomer(Customer toAdd);

    Customer updateCustomerById(UUID id, Customer toUpdate);

    void deleteCustomerById(UUID id);
}
