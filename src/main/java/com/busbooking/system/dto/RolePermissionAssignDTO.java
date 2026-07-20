package com.busbooking.system.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.Set;

@Data
public class RolePermissionAssignDTO {
    @NotEmpty(message = "Permission IDs list cannot be empty")
    private Set<Integer> permissionIds;
}
