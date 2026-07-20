package com.busbooking.system.repository;

import com.busbooking.system.entity.BusLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusLayoutRepository extends JpaRepository<BusLayout, Integer> { // [cite: 22]
    List<BusLayout> findByIsActiveTrue(); //
}