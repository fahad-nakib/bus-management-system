package com.busbooking.system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class LockExpiredException extends RuntimeException {
    public LockExpiredException(String message) {
        super(message);
    }
}