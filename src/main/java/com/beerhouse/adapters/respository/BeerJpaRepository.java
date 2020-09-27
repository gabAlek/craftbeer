package com.beerhouse.adapters.respository;

import com.beerhouse.domain.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerJpaRepository extends JpaRepository<Beer, Integer> {

}
