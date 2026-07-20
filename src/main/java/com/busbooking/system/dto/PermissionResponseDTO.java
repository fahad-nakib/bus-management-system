package com.busbooking.system.dto;

import lombok.Data;

@Data
public class PermissionResponseDTO {
    private Integer permissionId;
    private String permissionCode;
    private String module;
    private String description;
}
