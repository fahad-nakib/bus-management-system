package com.busbooking.system.repository;

import com.busbooking.system.entity.BookingPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingPassengerRepository extends JpaRepository<BookingPassenger, Long> {
}