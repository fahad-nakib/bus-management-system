package com.busbooking.system.service;

import com.busbooking.system.dto.PermissionRequestDTO;
import com.busbooking.system.dto.PermissionResponseDTO;
import java.util.List;

public interface PermissionService {
    PermissionResponseDTO createPermission(PermissionRequestDTO request);
    List<PermissionResponseDTO> getAllPermissions();
    PermissionResponseDTO getPermissionById(Integer id);
    PermissionResponseDTO updatePermission(Integer id, PermissionRequestDTO request);
    void deletePermission(Integer id);
}
