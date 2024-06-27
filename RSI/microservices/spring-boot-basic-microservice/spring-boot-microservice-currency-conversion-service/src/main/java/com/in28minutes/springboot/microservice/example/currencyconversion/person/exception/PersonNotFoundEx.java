package com.in28minutes.springboot.microservice.example.currencyconversion.person.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PersonNotFoundEx extends RuntimeException {
    public PersonNotFoundEx(Long id) {
        super("The person with id = " + id + " does not exist");
    }

    public PersonNotFoundEx() {
        super("This person does not exist");
    }
}
