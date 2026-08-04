package com.busbooking.system.repository;

import com.busbooking.system.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Integer> {
    List<RouteStop> findByRouteRouteIdOrderByStopOrderAsc(Integer routeId);

    @Modifying
    @Query("UPDATE RouteStop rs SET rs.stopOrder = rs.stopOrder + 1 WHERE rs.route.routeId = :routeId AND rs.stopOrder >= :fromOrder")
    void incrementStopOrdersFrom(@Param("routeId") Integer routeId, @Param("fromOrder") Short fromOrder);
}