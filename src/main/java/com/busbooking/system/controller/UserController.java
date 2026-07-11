package com.busbooking.system.controller;

import com.busbooking.system.dto.UserRegisterRequest;
import com.busbooking.system.entity.User;
import com.busbooking.system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest registrationDto) {
        try {
            User savedUser = userService.registerNewUser(registrationDto);
            // সিকিউরিটির জন্য রেসপন্সে পাসওয়ার্ড হ্যাশ না পাঠিয়ে একটি মেসেজ পাঠানো ভালো
            return new ResponseEntity<>("ইউজার রেজিস্ট্রেশন সফল হয়েছে! আইডি: " + savedUser.getUserId(), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // ফোন বা ইমেইল ডুপ্লিকেট হলে যে এক্সেপশন থ্রো করা হয়েছে তা হ্যান্ডেল করা
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}