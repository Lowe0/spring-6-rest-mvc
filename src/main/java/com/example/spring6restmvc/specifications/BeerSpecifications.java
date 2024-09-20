package com.example.spring6restmvc.specifications;

import com.example.spring6restmvc.entities.Beer;
import org.springframework.data.jpa.domain.Specification;

public class BeerSpecifications {

    public static Specification<Beer> beerNameLike(String nameFragment) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("beerName"), "%" + nameFragment + "%"));
    }
}
