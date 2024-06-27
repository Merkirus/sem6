package com.in28minutes.springboot.microservice.example.currencyconversion.person.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PersonExistsEx extends RuntimeException {
    public PersonExistsEx(Long id) {
        super("The person with id = " + id + " already exists");
    }

    public PersonExistsEx() {
        super("This person already exists");
    }
}
