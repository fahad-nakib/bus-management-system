package com.busbooking.system.service;

import com.busbooking.system.dto.UserRegisterRequestDTO;
import com.busbooking.system.dto.UserResponseDTO;
import java.util.List;

public interface UserService {
    UserResponseDTO registerUser(UserRegisterRequestDTO request);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
}
