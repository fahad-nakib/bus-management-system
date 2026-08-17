package com.busbooking.system.dto;

import com.busbooking.system.entity.enums.GenderEnum;

public record PassengerDTO(
        String name,
        GenderEnum gender,
        Short age,
        String contactPhone
) {}