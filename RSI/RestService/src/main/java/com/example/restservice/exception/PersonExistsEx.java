package com.example.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PersonExistsEx extends RuntimeException {
    public PersonExistsEx(int id) {
        super("The person with id = " + id + " already exists");
    }

    public PersonExistsEx() {
        super("This person already exists");
    }
}
