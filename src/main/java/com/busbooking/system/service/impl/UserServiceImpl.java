package com.busbooking.system.service.impl;

import com.busbooking.system.dto.PermissionResponseDTO;
import com.busbooking.system.dto.RoleResponseDTO;
import com.busbooking.system.dto.UserRegisterRequestDTO;
import com.busbooking.system.dto.UserResponseDTO;
import com.busbooking.system.entity.Role;
import com.busbooking.system.entity.User;
import com.busbooking.system.entity.enums.RoleNameEnum;
import com.busbooking.system.repository.RoleRepository;
import com.busbooking.system.repository.UserRepository;
import com.busbooking.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // পাসওয়ার্ড হ্যাশ করার জন্য
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRegisterRequestDTO request) {
        // ১. ইউনিকনেস চেক (Email & Phone)
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number is already registered!");
        }

        // ২. ডাটাবেজ থেকে ডিফল্ট PASSENGER রোলটি খুঁজে বের করা
        // নোট: যদি আপনার এনামে নাম ভিন্ন হয় (যেমন: ROLE_PASSENGER), তবে সেটি ব্যবহার করুন
        Role passengerRole = roleRepository.findByRoleName(RoleNameEnum.PASSENGER)
                .orElseThrow(() -> new RuntimeException("Default role 'PASSENGER' not found in the database. Please seed the roles first."));

        // ৩. নতুন ইউজার অবজেক্ট তৈরি এবং ডাটা সেট করা
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setNidOrPassport(request.getNidOrPassport());

        // পাসওয়ার্ড হ্যাশ বা এনক্রিপ্ট করা
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // ডিফল্ট ভ্যালু সেট (যদিও এনটিটিতে সেট করা আছে, সেফটির জন্য কোডেও দেওয়া ভালো)
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        user.setIsPhoneVerified(false);
        user.setCreatedAt(ZonedDateTime.now());

        // রোল অ্যাসাইন করা
        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(passengerRole);
        user.setRoles(defaultRoles);

        // ডাটাবেজে সেভ করা
        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // User Entity থেকে Response DTO তে কনভার্ট করার মেথড
    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setUserId(user.getUserId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setNidOrPassport(user.getNidOrPassport());
        response.setIsActive(user.getIsActive());
        response.setIsEmailVerified(user.getIsEmailVerified());
        response.setIsPhoneVerified(user.getIsPhoneVerified());
        response.setCreatedAt(user.getCreatedAt());

        if (user.getRoles() != null) {
            Set<RoleResponseDTO> roleDTOs = user.getRoles().stream()
                    .map(role -> {
                        RoleResponseDTO rDto = new RoleResponseDTO();
                        rDto.setRoleId(role.getRoleId());
                        rDto.setRoleName(role.getRoleName());
                        rDto.setDescription(role.getDescription());
                        rDto.setCreatedAt(role.getCreatedAt());
                        return rDto;
                    }).collect(Collectors.toSet());
            response.setRoles(roleDTOs);
        }
        return response;
    }
}
