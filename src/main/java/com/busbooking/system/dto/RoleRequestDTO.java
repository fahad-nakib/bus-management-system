package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.RoleNameEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequestDTO {

    @NotNull(message = "Role name is required")
    private RoleNameEnum roleName;

    private String description;
}
