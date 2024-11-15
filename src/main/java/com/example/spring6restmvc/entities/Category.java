package com.example.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "varchar(36)", length = 36, updatable = false, nullable = false)
    private UUID id;
    @Version
    private Long version;
    private String description;
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "beer_category",
    joinColumns = @JoinColumn(name = "category_id"),
    inverseJoinColumns = @JoinColumn(name="beer_id"))
    private Set<Beer> beers = new HashSet<>();
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdDate;
    @UpdateTimestamp
    private Instant lastModifiedDate;

    public void addBeer(Beer beer) {
        this.beers.add(beer);
    }
    public void removeBeer(Beer beer) {
        this.beers.remove(beer);
    }
}
