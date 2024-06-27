package com.example.restservice.person;

import com.example.restservice.exception.ConflictEx;
import com.example.restservice.exception.PersonExistsEx;
import com.example.restservice.exception.PersonNotFoundEx;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface IPersonService {
    public abstract CollectionModel<EntityModel<Person>> getAllPersons();
    public abstract EntityModel<Person> getPerson(int id) throws PersonNotFoundEx;
    public abstract EntityModel<Person> addPerson(Person person) throws PersonExistsEx;
    public abstract EntityModel<Person> updatePerson(int id, Person person) throws PersonNotFoundEx;
    public abstract Boolean deletePerson(int id) throws PersonNotFoundEx;
    public abstract EntityModel<Person> hirePerson(int id) throws ConflictEx;
    public abstract EntityModel<Person> vacatePerson(int id) throws ConflictEx;
    public abstract EntityModel<Person> deactivatePerson(int id) throws ConflictEx;
    public abstract EntityModel<Person> activatePerson(int id) throws ConflictEx;
}
