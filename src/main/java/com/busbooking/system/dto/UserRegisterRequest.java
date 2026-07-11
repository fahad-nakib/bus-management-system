package com.busbooking.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank(message = "নাম খালি রাখা যাবে না")
    @Size(max = 150, message = "নাম ১৫০ অক্ষরের বেশি হতে পারবে না")
    private String fullName;

    @NotBlank(message = "মোবাইল নম্বর দিতেই হবে")
    @Pattern(regexp = "^(?:\\+88|88)?01[3-9]\\d{8}$", message = "সঠিক বাংলাদেশী মোবাইল নম্বর দিন")
    @Size(max = 20, message = "মোবাইল নম্বর ২০ অক্ষরের বেশি হতে পারবে না")
    private String phoneNumber;

    @NotBlank(message = "পাসওয়ার্ড দিতেই হবে")
    @Size(min = 6, max = 50, message = "পাসওয়ার্ড অন্তত ৬ থেকে ৫০ অক্ষরের হতে হবে")
    private String password;

    @Email(message = "সঠিক ইমেইল অ্যাড্রেস দিন")
    @Size(max = 150, message = "ইমেইল ১৫০ অক্ষরের বেশি হতে পারবে না")
    private String email; // এটি অপশনাল হতে পারে, তাই @NotBlank দেওয়া হয়নি

    @Size(max = 50, message = "এনআইডি বা পাসপোর্ট নম্বর ৫০ অক্ষরের বেশি হতে পারবে না")
    private String nidOrPassport;
}