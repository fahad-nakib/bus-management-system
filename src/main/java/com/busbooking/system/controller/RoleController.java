package com.busbooking.system.controller;

import com.busbooking.system.dto.RolePermissionAssignDTO;
import com.busbooking.system.dto.RoleRequestDTO;
import com.busbooking.system.dto.RoleResponseDTO;
import com.busbooking.system.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO request) {
        RoleResponseDTO response = roleService.createRole(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        List<RoleResponseDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Integer id) {
        RoleResponseDTO response = roleService.getRoleById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Integer id,
            @Valid @RequestBody RoleRequestDTO request) {
        RoleResponseDTO response = roleService.updateRole(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role deleted successfully with id: " + id);
    }

    // রোলের সাথে পারমিশন অ্যাসাইন বা আপডেট করার এন্ডপয়েন্ট
    // URL Example: PUT /api/v1/roles/1/permissions
    // Request Body: { "permissionIds": [1, 2, 3] }
    @PutMapping("/{id}/permissions")
    public ResponseEntity<RoleResponseDTO> assignPermissions(
            @PathVariable Integer id,
            @Valid @RequestBody RolePermissionAssignDTO assignDTO) {
        RoleResponseDTO response = roleService.assignPermissionsToRole(id, assignDTO);
        return ResponseEntity.ok(response);
    }
}
