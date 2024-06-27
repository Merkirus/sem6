package com.example.restservice.person;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/persons")
@RestController
public class PersonController {
    private final IPersonService personService;

    public PersonController(IPersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Person>>> getPersons() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.getAllPersons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Person>> getPerson(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.getPerson(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Person>> updatePerson(@PathVariable("id") int id, @RequestBody Person person) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.updatePerson(id, person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePerson(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.deletePerson(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Person>> addPerson(@RequestBody Person person) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.addPerson(person));
    }

    @PatchMapping("/{id}/hire")
    public ResponseEntity<EntityModel<Person>> hirePerson(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.hirePerson(id));
    }

    @PatchMapping("/{id}/vacate")
    public ResponseEntity<EntityModel<Person>> vacatePerson(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.vacatePerson(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<EntityModel<Person>> deactivatePerson(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.deactivatePerson(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<EntityModel<Person>> activatePerson(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.activatePerson(id));
    }
}
