package com.busbooking.system.repository;

import com.busbooking.system.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    boolean existsByOriginCityAndDestinationCity(String originCity, String destinationCity);
}