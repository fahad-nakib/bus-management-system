package com.busbooking.system.repository;

import com.busbooking.system.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    // আপডেট করার সময় পুরানো সিট মুছে ফেলার জন্য
    void deleteByBusLayoutLayoutId(Integer layoutId);
}