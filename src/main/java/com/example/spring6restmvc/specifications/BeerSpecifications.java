package com.example.spring6restmvc.specifications;

import com.example.spring6restmvc.entities.Beer;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class BeerSpecifications {

    public static Specification<Beer> beerNameLike(String nameFragment) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("beerName")), "%" + nameFragment.toLowerCase(Locale.ROOT) + "%"));
    }
}
