package com.busbooking.system.repository;

import com.busbooking.system.entity.BoardingDroppingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardingDroppingPointRepository extends JpaRepository<BoardingDroppingPoint, Integer> {
    List<BoardingDroppingPoint> findByRouteStopRouteStopId(Integer routeStopId);
    List<BoardingDroppingPoint> findByRouteStopRouteRouteId(Integer routeId);
    List<BoardingDroppingPoint> findByRouteStopRouteStopIdIn(List<Integer> routeStopIds);
}