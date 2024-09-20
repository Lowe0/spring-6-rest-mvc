package com.example.spring6restmvc.specifications;

import com.example.spring6restmvc.entities.Beer;
import com.example.spring6restmvc.model.BeerStyle;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class BeerSpecifications {

    public static Specification<Beer> beerNameLike(String nameLikeExpression) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("beerName")), nameLikeExpression.toLowerCase(Locale.ROOT)));
    }

    public static Specification<Beer> beerStyleEquals(BeerStyle beerStyle) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("beerStyle"), beerStyle));
    }
}
