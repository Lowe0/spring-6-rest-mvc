package com.example.spring6restmvc.repositories;

import com.example.spring6restmvc.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID>, JpaSpecificationExecutor<BeerOrder> {
}
