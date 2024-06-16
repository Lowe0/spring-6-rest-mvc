package com.example.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "varchar", length = 36, updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
