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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> listCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        return Optional.ofNullable(customerRepository.findById(id).orElse(null)).map(customerMapper::customerToCustomerDto);
    }

    @Override
    public CustomerDto addCustomer(CustomerDto toAdd) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(toAdd)));
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto toUpdate) {
        var refUpdated = new AtomicReference<Optional<CustomerDto>>();
        customerRepository.findById(id).ifPresentOrElse(customer -> {
            customer.setCustomerName(toUpdate.getCustomerName());
            var savedCustomer = customerRepository.save(customer);
            refUpdated.set(Optional.of(customerMapper.customerToCustomerDto(savedCustomer)));
        }, () -> refUpdated.set(Optional.empty()));
        return refUpdated.get();
    }

    @Override
    public Optional<CustomerDto> deltaCustomerById(UUID id, CustomerDto toUpdate) {
        var refUpdated = new AtomicReference<Optional<CustomerDto>>();
        customerRepository.findById(id).ifPresentOrElse(customer -> {
            customerMapper.deltaCustomerToCustomerDto(customer, toUpdate);
            var savedCustomer = customerRepository.save(customer);
            refUpdated.set(Optional.of(customerMapper.customerToCustomerDto(savedCustomer)));
        }, () -> refUpdated.set(Optional.empty()));
        return refUpdated.get();
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
