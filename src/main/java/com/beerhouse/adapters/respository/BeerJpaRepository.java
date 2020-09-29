package com.beerhouse.adapters.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface BeerJpaRepository extends JpaRepository<BeerEntity, Integer> {

    List<BeerEntity> findAll();

    Optional<BeerEntity> findById(int id);

    BeerEntity save(BeerEntity beer);

    Long deleteById(Integer id);

    boolean existsBeerEntityByName(String name);
}
