package com.example.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestEx extends RuntimeException {
    public BadRequestEx() {
        super("Bad request");
    }

    public BadRequestEx(String message) {
        super(message);
    }
}
