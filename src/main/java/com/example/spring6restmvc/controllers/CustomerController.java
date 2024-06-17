package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.model.CustomerDto;
import com.example.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CustomerController {
    private final CustomerService customerService;
    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDto> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDto getCustomerById(@PathVariable("customerId") UUID id) {
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity addCustomer(@RequestBody CustomerDto toAdd) {
        CustomerDto savedCustomer = customerService.addCustomer(toAdd);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + savedCustomer.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDto toUpdate) {
        customerService.updateCustomerById(id, toUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + id);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = CUSTOMER_PATH_ID)
    public ResponseEntity deltaCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDto toUpdate) {
        customerService.deltaCustomerById(id, toUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + id);

        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID id) {
        customerService.deleteCustomerById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
