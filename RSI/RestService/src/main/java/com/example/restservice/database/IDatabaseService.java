package com.example.restservice.database;

import com.example.restservice.person.Person;
import com.example.restservice.exception.PersonExistsEx;
import com.example.restservice.exception.PersonNotFoundEx;

import java.util.List;

public interface IDatabaseService {
    abstract public List<Person> getAllPersons();
    abstract public Person getPerson(int id) throws PersonNotFoundEx;
    abstract public Person updatePerson(Person person) throws PersonNotFoundEx;
    abstract public boolean deletePerson(int id) throws PersonNotFoundEx;
    abstract public Person addPerson(Person person) throws PersonExistsEx;
}
