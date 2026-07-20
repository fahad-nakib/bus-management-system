package com.busbooking.system.service;

import com.busbooking.system.dto.RolePermissionAssignDTO;
import com.busbooking.system.dto.RoleRequestDTO;
import com.busbooking.system.dto.RoleResponseDTO;
import java.util.List;

public interface RoleService {
    RoleResponseDTO createRole(RoleRequestDTO request);
    List<RoleResponseDTO> getAllRoles();
    RoleResponseDTO getRoleById(Integer id);
    RoleResponseDTO updateRole(Integer id, RoleRequestDTO request);
    void deleteRole(Integer id);

    // রোল এবং পারমিশন লিঙ্ক করার স্পেশাল মেথড
    RoleResponseDTO assignPermissionsToRole(Integer roleId, RolePermissionAssignDTO assignDTO);
}
