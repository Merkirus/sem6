package com.example.restservice.exception;

import com.example.restservice.person.PersonStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictEx extends RuntimeException {
    public ConflictEx(int id, PersonStatus status) {
        super("The person with id = " + id + " conflict status - " + status.toString());
    }

    public ConflictEx() {
        super("This person has conflict status");
    }
}
