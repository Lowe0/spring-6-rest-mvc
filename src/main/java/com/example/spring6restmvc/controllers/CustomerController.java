package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.model.Customer;
import com.example.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {
    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity addCustomer(@RequestBody Customer toAdd) {
        Customer savedCustomer = customerService.addCustomer(toAdd);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{customerId}")
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID id, @RequestBody Customer toUpdate) {
        Customer savedCustomer = customerService.updateCustomerById(id, toUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + id);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PatchMapping(value="/{customerId}")
    public ResponseEntity deltaCustomerById(@PathVariable("customerId") UUID id, @RequestBody Customer toUpdate) {
        Customer savedCustomer = customerService.deltaCustomerById(id, toUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + id);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value="/{customerId}")
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID id){
        customerService.deleteCustomerById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
