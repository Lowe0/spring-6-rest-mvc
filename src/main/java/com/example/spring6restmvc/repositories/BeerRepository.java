package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID>, JpaSpecificationExecutor<Beer> {
}
