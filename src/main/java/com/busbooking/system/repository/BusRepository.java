package com.busbooking.system.repository;

import com.busbooking.system.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {
    boolean existsByBusNumber(String busNumber);
    boolean existsByRegistrationNumber(String registrationNumber);
    boolean existsByBusNumberAndBusIdNot(String busNumber, Integer busId);
    boolean existsByRegistrationNumberAndBusIdNot(String registrationNumber, Integer busId);
    List<Bus> findByIsActive(Boolean isActive);
}