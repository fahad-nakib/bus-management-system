package com.busbooking.system.service;

import com.busbooking.system.dto.UserRegisterRequest;
import com.busbooking.system.entity.User;

public interface UserService {
    User registerNewUser(UserRegisterRequest registerRequest);
}
