package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, CustomerDto> customerMap;

    public CustomerServiceImpl() {
        populateMap();
    }

    private void populateMap() {
        this.customerMap = new HashMap<>();

        CustomerDto kevin = CustomerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Kevin Lowe")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now()).build();

        CustomerDto cassi = CustomerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Cassi Lowe")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now()).build();

        customerMap.put(kevin.getId(), kevin);
        customerMap.put(cassi.getId(), cassi);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        log.debug("Customer Service - Get Customers");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        log.debug("Customer Service - Get Customer - " + id);
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDto addCustomer(CustomerDto toAdd) {
        CustomerDto savedCustomer = CustomerDto.builder()
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
    public Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto toUpdate) {
        CustomerDto savedCustomer = customerMap.get(id);
        savedCustomer.setCustomerName(toUpdate.getCustomerName());
        savedCustomer.setVersion(savedCustomer.getVersion() + 1);
        savedCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(id, savedCustomer);
        return Optional.of(savedCustomer);
    }

    @Override
    public Optional<CustomerDto> deltaCustomerById(UUID id, CustomerDto toUpdate) {
        CustomerDto savedCustomer = customerMap.get(id);
        if (StringUtils.hasText(toUpdate.getCustomerName())) {
            savedCustomer.setCustomerName(toUpdate.getCustomerName());
        }
        savedCustomer.setVersion(savedCustomer.getVersion() + 1);
        savedCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(id, savedCustomer);
        return Optional.of(savedCustomer);
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        customerMap.remove(id);
        return true;
    }
}
