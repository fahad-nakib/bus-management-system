package com.busbooking.system.controller;

import com.busbooking.system.dto.UserRegisterRequestDTO;
import com.busbooking.system.dto.UserResponseDTO;
import com.busbooking.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "User Authentication API", description = "This API contain user register, find user by Id")
public class AuthController {

    private final UserService userService;

    // ইউজার রেজিস্ট্রেশন এন্ডপয়েন্ট
    // URL: POST /api/v1/auth/register
    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Using this endpoint user can register to the system")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO request) {
        UserResponseDTO response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // নির্দিষ্ট ইউজারের প্রোফাইল দেখার এন্ডপয়েন্ট (ম্যানেজমেন্ট বা টেস্টের জন্য)
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // সব ইউজারের লিস্ট দেখার এন্ডপয়েন্ট (Admin Only হওয়া উচিত পরবর্তীতে)
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
