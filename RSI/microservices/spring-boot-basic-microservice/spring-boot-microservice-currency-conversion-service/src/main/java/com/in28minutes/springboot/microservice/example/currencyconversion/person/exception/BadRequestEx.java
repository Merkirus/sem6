package com.in28minutes.springboot.microservice.example.currencyconversion.person.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestEx extends RuntimeException {
    public BadRequestEx(String message) {
        super(message);
    }

    public BadRequestEx() {
        super("Bad request");
    }
}
