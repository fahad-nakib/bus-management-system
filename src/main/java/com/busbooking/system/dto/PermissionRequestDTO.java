package com.busbooking.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermissionRequestDTO {

    @NotBlank(message = "Permission code cannot be blank")
    @Size(max = 100, message = "Permission code must be within 100 characters")
    private String permissionCode;

    @NotBlank(message = "Module name cannot be blank")
    @Size(max = 50, message = "Module name must be within 50 characters")
    private String module;

    private String description;
}
