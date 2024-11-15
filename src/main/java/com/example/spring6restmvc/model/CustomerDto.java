package com.example.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class CustomerDto {
    private UUID id;
    private Integer version;
    private String customerName;
    private String email;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
