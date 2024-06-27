package org.example;

import java.util.List;

public interface PersonRepository2 {
    List<Person> getAllPerson();
    Person getPerson(int id) throws PersonNotFoundEx_Exception;
    Person addPerson(int id, String name, int age) throws PersonExistsEx_Exception;
    boolean deletePerson(int id) throws PersonNotFoundEx_Exception;
    Person updatePerson(int id, String name, int age) throws PersonNotFoundEx_Exception;
    int countPersons();
    boolean clearPersons();
}
