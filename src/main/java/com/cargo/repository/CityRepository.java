package com.cargo.repository;

import com.cargo.model.City;
import com.cargo.model.enums.Region;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);

    @Query("FROM City c WHERE c.region = :region")
    List<City> findAllByRegion(Region region, Sort sort);

}
