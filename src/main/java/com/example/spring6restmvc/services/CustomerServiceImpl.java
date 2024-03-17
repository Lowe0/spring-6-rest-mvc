package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        populateMap();
    }

    private void populateMap() {
        this.customerMap = new HashMap<>();

        Customer kevin = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Kevin Lowe")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now()).build();

        Customer cassi = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Cassi Lowe")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now()).build();

        customerMap.put(kevin.getId(), kevin);
        customerMap.put(cassi.getId(), cassi);
    }

    @Override
    public Iterable<Customer> listCustomers() {
        log.debug("Customer Service - Get Customers");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        log.debug("Customer Service - Get Customer - " + id);
        return customerMap.get(id);
    }

    @Override
    public Customer addCustomer(Customer toAdd) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .customerName(toAdd.getCustomerName())
                .version(toAdd.getVersion())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public Customer updateCustomerById(UUID id, Customer toUpdate) {
        Customer savedCustomer = customerMap.get(id);
        savedCustomer.setCustomerName(toUpdate.getCustomerName());
        savedCustomer.setVersion(savedCustomer.getVersion() + 1);
        savedCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(id, savedCustomer);

        return savedCustomer;
    }

    @Override
    public void deleteCustomerById(UUID id) {
        customerMap.remove(id);
    }
}
