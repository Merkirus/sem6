package com.in28minutes.springboot.microservice.example.currencyconversion.person;

import com.in28minutes.springboot.microservice.example.currencyconversion.person.status.IPersonStatusService;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.status.PersonStatus;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/persons")
@CrossOrigin(origins = "*")
@RestController
public class PersonController {
    private final IPersonService personService;
    private final IPersonStatusService personStatusService;

    public PersonController(IPersonService personService, IPersonStatusService personStatusService) {
        this.personService = personService;
        this.personStatusService = personStatusService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Person>>> getPersons() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.getPersons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Person>> getPerson(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.getPerson(id));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Person>> addPerson(@RequestBody PersonWebView person) {
        var state = personStatusService.getPersonStatuses().stream().filter(_ps -> Objects.equals(_ps.getName(), person.getStatus())).findAny();
        PersonStatus ps;
        if (state.isPresent()) {
            ps = state.get();
        } else {
            PersonStatus new_ps = new PersonStatus(person.getName());
            personStatusService.addPersonStatus(new_ps);
            ps = new_ps;
        }
        Person _person = new Person(person.getId(), person.getName(), person.getAge(), ps);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.addPerson(_person));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Person>> updatePerson(@PathVariable("id") Long id, @RequestBody PersonWebView person) {
        var state = personStatusService.getPersonStatuses().stream().filter(_ps -> Objects.equals(_ps.getName(), person.getStatus())).findAny();
        PersonStatus ps;
        if (state.isPresent()) {
            ps = state.get();
        } else {
            PersonStatus new_ps = new PersonStatus(person.getStatus());
            personStatusService.addPersonStatus(new_ps);
            ps = new_ps;
        }
        Person _person = new Person(person.getId(), person.getName(), person.getAge(), ps);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.updatePerson(id, _person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<Boolean>> deletePerson(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.deletePerson(id));
    }

    @PatchMapping("/{id}/hire")
    public ResponseEntity<EntityModel<Person>> hirePerson(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.hirePerson(id));
    }

    @PatchMapping("/{id}/vacate")
    public ResponseEntity<EntityModel<Person>> vacatePerson(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.vacatePerson(id));
    }
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<EntityModel<Person>> deactivatePerson(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.deactivatePerson(id));
    }
    @PatchMapping("/{id}/activate")
    public ResponseEntity<EntityModel<Person>> activatePerson(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.activatePerson(id));
    }

    @PatchMapping("/{id}/encrypt")
    public ResponseEntity<EntityModel<Person>> encryptPerson(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.encryptPerson(id));
    }

    @PatchMapping("/{id}/decrypt")
    public ResponseEntity<EntityModel<Person>> decryptPerson(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(personService.decryptPerson(id));
    }
}
