package com.busbooking.system.service.impl;

import com.busbooking.system.dto.UserRegisterRequest;
import com.busbooking.system.entity.User;
import com.busbooking.system.repository.UserRepository;
import com.busbooking.system.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // কনস্ট্রাক্টর ইনজেকশন (Autowired এর চেয়ে এটি বেশি রিকমেন্ডেড)
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // কোনো এরর হলে ডাটা রোলব্যাক করার জন্য
    public User registerNewUser(UserRegisterRequest registerRequest) {

        // ১. ফোন নম্বর অলরেডি আছে কি না চেক করা
        if (userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new RuntimeException("এই মোবাইল নম্বরটি দিয়ে ইতিপূর্ধেই অ্যাকাউন্ট খোলা হয়েছে!");
        }

        // ২. ইমেইল দেওয়া থাকলে সেটি অলরেডি আছে কি না চেক করা
        if (registerRequest.getEmail() != null && !registerRequest.getEmail().isBlank()) {
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new RuntimeException("এই ইমেইলটি ইতিপূর্ধেই ব্যবহার করা হয়েছে!");
            }
        }

        // ৩. নতুন ইউজার এন্টিটি অবজেক্ট তৈরি করা
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setEmail(registerRequest.getEmail());
        user.setNidOrPassport(registerRequest.getNidOrPassport());

        // ৪. পাসওয়ার্ড হ্যাশ (Encrypt) করা
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPasswordHash(hashedPassword);

        // ৫. ডাটাবেজে সেভ করা এবং রিটার্ন করা
        // (নোট: isActive, isEmailVerified, isPhoneVerified, createdAt এগুলো অটো সেট হবে)
        return userRepository.save(user);
    }
}