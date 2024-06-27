package com.in28minutes.springboot.microservice.example.currencyconversion.person;

import com.in28minutes.springboot.microservice.example.currencyconversion.person.exception.ConflictEx;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.exception.PersonExistsEx;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.exception.PersonNotFoundEx;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.transaction.annotation.Transactional;

public interface IPersonService {
    public abstract CollectionModel<EntityModel<Person>> getPersons();
    public abstract EntityModel<Person> getPerson(Long id) throws PersonNotFoundEx;
    @Transactional
    public abstract EntityModel<Person> addPerson(Person person) throws PersonExistsEx;
    @Transactional
    public abstract EntityModel<Person> updatePerson(Long id, Person person) throws PersonNotFoundEx;
    @Transactional
    public abstract EntityModel<Boolean> deletePerson(Long id) throws PersonNotFoundEx;
    @Transactional
    public abstract EntityModel<Person> hirePerson(Long id) throws ConflictEx;
    @Transactional
    public abstract EntityModel<Person> vacatePerson(Long id) throws ConflictEx;
    @Transactional
    public abstract EntityModel<Person> deactivatePerson(Long id) throws ConflictEx;
    @Transactional
    public abstract EntityModel<Person> activatePerson(Long id) throws ConflictEx;
    @Transactional
    public abstract EntityModel<Person> encryptPerson(Long id);
    @Transactional
    public abstract EntityModel<Person> decryptPerson(Long id);
}
