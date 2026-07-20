package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.RoleNameEnum;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class RoleResponseDTO {
    private Integer roleId;
    private RoleNameEnum roleName;
    private String description;
    private ZonedDateTime createdAt;
    private Set<PermissionResponseDTO> permissions; // রোলের সাথে কী কী পারমিশন আছে তাও দেখা যাবে
}

