package com.example.spring6restmvc.services;

import com.example.spring6restmvc.mappers.CustomerMapper;
import com.example.spring6restmvc.model.CustomerDto;
import com.example.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> listCustomers() {
        return null;
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        return Optional.empty();
    }

    @Override
    public CustomerDto addCustomer(CustomerDto toAdd) {
        return null;
    }

    @Override
    public void updateCustomerById(UUID id, CustomerDto toUpdate) {

    }

    @Override
    public void deltaCustomerById(UUID id, CustomerDto toUpdate) {

    }

    @Override
    public void deleteCustomerById(UUID id) {

    }
}
