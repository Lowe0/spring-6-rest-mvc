package com.example.spring6restmvc.controllers;

import com.example.spring6restmvc.model.CustomerDto;
import com.example.spring6restmvc.services.CustomerService;
import com.example.spring6restmvc.services.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.spring6restmvc.controllers.CustomerController.CUSTOMER_PATH;
import static com.example.spring6restmvc.controllers.CustomerController.CUSTOMER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDto> customerCaptor;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

    @Test
    void listCustomers() throws Exception {
        // this works fine for now, until customerService points to an actual data store
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(CUSTOMER_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(customerService).listCustomers();
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDto testCustomer = customerServiceImpl.listCustomers().iterator().next();
        // again, this works fine for now, until we have a real data store
        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));
        mockMvc.perform(get(CUSTOMER_PATH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testCustomer.getId().toString()))
                .andExpect(jsonPath("$.customerName").value(testCustomer.getCustomerName()));

        verify(customerService).getCustomerById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(get(CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void addCustomer() throws Exception {
        CustomerDto testCustomer = customerServiceImpl.listCustomers().iterator().next();
        testCustomer.setVersion(null);
        testCustomer.setId(null);

        given(customerService.addCustomer(any(CustomerDto.class))).willReturn(customerServiceImpl.listCustomers().iterator().next());
        mockMvc.perform(post(CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        verify(customerService).addCustomer(customerCaptor.capture());
        assertThat(customerCaptor.getValue()).isEqualTo(testCustomer);
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDto testCustomer = customerServiceImpl.listCustomers().iterator().next();

        mockMvc.perform(put(CUSTOMER_PATH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), customerCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
    }

    @Test
    void deleteCustomer() throws Exception {
        CustomerDto testCustomer = customerServiceImpl.listCustomers().iterator().next();

        mockMvc.perform(delete(CUSTOMER_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
    }

    @Test
    void deltaCustomer() throws Exception {
        CustomerDto testCustomer = customerServiceImpl.listCustomers().iterator().next();

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Name");

        mockMvc.perform(patch(CUSTOMER_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).deltaCustomerById(uuidArgumentCaptor.capture(), customerCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testCustomer.getId());
        assertThat(customerCaptor.getValue().getCustomerName()).isEqualTo(customerMap.get("customerName"));
    }
}