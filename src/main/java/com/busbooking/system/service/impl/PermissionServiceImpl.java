package com.busbooking.system.service.impl;

import com.busbooking.system.dto.PermissionRequestDTO;
import com.busbooking.system.dto.PermissionResponseDTO;
import com.busbooking.system.entity.Permission;
import com.busbooking.system.repository.PermissionRepository;
import com.busbooking.system.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public PermissionResponseDTO createPermission(PermissionRequestDTO request) {
        if (permissionRepository.existsByPermissionCode(request.getPermissionCode())) {
            throw new RuntimeException("Permission code already exists: " + request.getPermissionCode());
        }

        Permission permission = new Permission();
        permission.setPermissionCode(request.getPermissionCode().toUpperCase()); // কোড সব সময় আপারকেস রাখা ভালো
        permission.setModule(request.getModule().toUpperCase());
        permission.setDescription(request.getDescription());

        Permission savedPermission = permissionRepository.save(permission);
        return mapToResponseDTO(savedPermission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponseDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponseDTO getPermissionById(Integer id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        return mapToResponseDTO(permission);
    }

    @Override
    @Transactional
    public PermissionResponseDTO updatePermission(Integer id, PermissionRequestDTO request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

        // যদি কোড পরিবর্তন করা হয়, তবে চেক করা যে নতুন কোডটি অন্য কারও আছে কিনা
        if (!permission.getPermissionCode().equalsIgnoreCase(request.getPermissionCode()) &&
                permissionRepository.existsByPermissionCode(request.getPermissionCode())) {
            throw new RuntimeException("Permission code already exists: " + request.getPermissionCode());
        }

        permission.setPermissionCode(request.getPermissionCode().toUpperCase());
        permission.setModule(request.getModule().toUpperCase());
        permission.setDescription(request.getDescription());

        Permission updatedPermission = permissionRepository.save(permission);
        return mapToResponseDTO(updatedPermission);
    }

    @Override
    @Transactional
    public void deletePermission(Integer id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

        // এখানে রোল চেক করা দরকার নেই কারণ @ManyToMany(mappedBy = "permissions") এর কারণে
        // আপনি ডিলিট করলে এটি স্বয়ংক্রিয়ভাবে role_permissions জয়েন টেবিল থেকেও রিমুভ হয়ে যাবে।
        permissionRepository.delete(permission);
    }

    // Entity থেকে DTO তে কনভার্ট করার হেল্পার মেথড (আপনি চাইলে ModelMapper বা MapStruct ব্যবহার করতে পারেন)
    private PermissionResponseDTO mapToResponseDTO(Permission permission) {
        PermissionResponseDTO response = new PermissionResponseDTO();
        response.setPermissionId(permission.getPermissionId());
        response.setPermissionCode(permission.getPermissionCode());
        response.setModule(permission.getModule());
        response.setDescription(permission.getDescription());
        return response;
    }
}
