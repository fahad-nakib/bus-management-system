package com.busbooking.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(max = 150, message = "Full name must be within 150 characters")
    private String fullName;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must be within 150 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?:\\+88|88)?01[3-9]\\d{8}$", message = "সঠিক বাংলাদেশী মোবাইল নম্বর দিন")
    @Size(max = 20, message = "Phone number must be within 20 characters")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @Size(max = 50, message = "NID or Passport must be within 50 characters")
    private String nidOrPassport;
}
