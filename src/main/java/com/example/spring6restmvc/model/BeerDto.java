package com.example.spring6restmvc.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class BeerDto {
    private UUID id;
    private Integer version;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String beerName;
    @NotNull
    private BeerStyle beerStyle;
    @NotNull
    @NotBlank
    private String upc;
    private Integer quantityOnHand;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
