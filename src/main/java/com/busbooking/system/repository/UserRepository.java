package com.busbooking.system.repository;

import com.busbooking.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ফোন নম্বর দিয়ে ইউজার খোঁজার জন্য (রেজিস্ট্রেশন ও লগইনে লাগবে)
    Optional<User> findByPhoneNumber(String phoneNumber);

    // ইমেইল দিয়ে ইউজার খোঁজার জন্য
    Optional<User> findByEmail(String email);

    // ফোন নম্বর অলরেডি ডাটাবেজে আছে কি না তা চেক করার জন্য
    boolean existsByPhoneNumber(String phoneNumber);

    // ইমেইল অলরেডি ডাটাবেজে আছে কি না তা চেক করার জন্য
    boolean existsByEmail(String email);
}