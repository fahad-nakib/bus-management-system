package com.busbooking.system.dto;

import lombok.Data;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String nidOrPassport;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private Boolean isPhoneVerified;
    private ZonedDateTime createdAt;
    private Set<RoleResponseDTO> roles; // ইউজারের কী কী রোল আছে তা দেখার জন্য
}
