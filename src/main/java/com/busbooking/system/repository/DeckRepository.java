package com.busbooking.system.repository;

import com.busbooking.system.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer> {
    // আপডেট করার সময় পুরানো ডেক মুছে ফেলার জন্য
    void deleteByBusLayoutLayoutId(Integer layoutId);
}