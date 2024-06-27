package com.in28minutes.springboot.microservice.example.currencyconversion.person.exception;

import com.in28minutes.springboot.microservice.example.currencyconversion.person.status.PersonStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictEx extends RuntimeException {
    public ConflictEx(Long id, PersonStatus status) {
        super("The person with id = " + id + " conflict status - " + status.getName());
    }

    public ConflictEx() {
        super("This person has conflict status");
    }
}
