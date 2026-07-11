package com.busbooking.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // এই নতুন মেথডটি যোগ করুন ডিফল্ট সিকিউরিটি লক বন্ধ করার জন্য
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // পোস্টম্যান টেস্টের জন্য CSRF ডিজেবল করা আবশ্যক
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // আপাতত সব এপিআই লক ছাড়া অ্যাক্সেস করার অনুমতি দেওয়া হলো
                );

        return http.build();
    }
}