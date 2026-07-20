package com.busbooking.system.service.impl;

import com.busbooking.system.dto.PermissionResponseDTO;
import com.busbooking.system.dto.RolePermissionAssignDTO;
import com.busbooking.system.dto.RoleRequestDTO;
import com.busbooking.system.dto.RoleResponseDTO;
import com.busbooking.system.entity.Permission;
import com.busbooking.system.entity.Role;
import com.busbooking.system.repository.PermissionRepository;
import com.busbooking.system.repository.RoleRepository;
import com.busbooking.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public RoleResponseDTO createRole(RoleRequestDTO request) {
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new RuntimeException("Role already exists with name: " + request.getRoleName());
        }

        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
        role.setCreatedAt(ZonedDateTime.now());
        role.setPermissions(new HashSet<>()); // শুরুতে পারমিশন খালি থাকবে

        Role savedRole = roleRepository.save(role);
        return mapToResponseDTO(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return mapToResponseDTO(role);
    }

    @Override
    @Transactional
    public RoleResponseDTO updateRole(Integer id, RoleRequestDTO request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if (role.getRoleName() != request.getRoleName() && roleRepository.existsByRoleName(request.getRoleName())) {
            throw new RuntimeException("Role already exists with name: " + request.getRoleName());
        }

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());

        Role updatedRole = roleRepository.save(role);
        return mapToResponseDTO(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Many-to-Many সম্পর্কের জন্য ডিলিট করলে জয়েন টেবিল (role_permissions) থেকে ডাটা নিজে থেকেই মুছে যাবে
        roleRepository.delete(role);
    }

    @Override
    @Transactional
    public RoleResponseDTO assignPermissionsToRole(Integer roleId, RolePermissionAssignDTO assignDTO) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        // আইডিগুলো দিয়ে ডাটাবেজ থেকে সব পারমিশন একসাথে তুলে আনা
        List<Permission> permissions = permissionRepository.findAllById(assignDTO.getPermissionIds());

        if (permissions.size() != assignDTO.getPermissionIds().size()) {
            throw new RuntimeException("One or more Permission IDs are invalid");
        }

        // রোলের ভেতরে পারমিশন সেট করে দেওয়া (পুরনো পারমিশন রিপ্লেস হয়ে নতুনগুলো সেট হবে)
        role.setPermissions(new HashSet<>(permissions));
        Role updatedRole = roleRepository.save(role);

        return mapToResponseDTO(updatedRole);
    }

    // Role Entity থেকে DTO তে রূপান্তর করার মেথড
    private RoleResponseDTO mapToResponseDTO(Role role) {
        RoleResponseDTO response = new RoleResponseDTO();
        response.setRoleId(role.getRoleId());
        response.setRoleName(role.getRoleName());
        response.setDescription(role.getDescription());
        response.setCreatedAt(role.getCreatedAt());

        if (role.getPermissions() != null) {
            Set<PermissionResponseDTO> permissionDTOs = role.getPermissions().stream()
                    .map(p -> {
                        PermissionResponseDTO pDto = new PermissionResponseDTO();
                        pDto.setPermissionId(p.getPermissionId());
                        pDto.setPermissionCode(p.getPermissionCode());
                        pDto.setModule(p.getModule());
                        pDto.setDescription(p.getDescription());
                        return pDto;
                    }).collect(Collectors.toSet());
            response.setPermissions(permissionDTOs);
        }
        return response;
    }
}
