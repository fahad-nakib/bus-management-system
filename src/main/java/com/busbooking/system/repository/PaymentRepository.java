package com.busbooking.system.repository;

import com.busbooking.system.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.transactionReference = :transactionReference")
    Optional<Payment> findByTransactionReferenceWithLock(@Param("transactionReference") String transactionReference);

    Optional<Payment> findByTransactionReference(String transactionReference);
}
