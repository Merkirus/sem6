package org.example;

import java.util.ArrayList;
import java.util.List;

public class PersonRepositoryImpl2 implements PersonRepository2 {
    private List<Person> personList;

    public PersonRepositoryImpl2() {
        personList = new ArrayList<>();
    }

    @Override
    public List<Person> getAllPerson() {
        return personList;
    }

    @Override
    public Person getPerson(int id) throws PersonNotFoundEx_Exception {
        for (Person thePerson : personList) {
            if (thePerson.getId() == id) {
                return thePerson;
            }
        }
        throw new PersonNotFoundEx_Exception("Error", new PersonNotFoundEx());
    }

    @Override
    public Person addPerson(int id, String name, int age) throws PersonExistsEx_Exception {
        for (Person thePerson : personList) {
            if (thePerson.getId() == id) {
                throw new PersonExistsEx_Exception("Error", new PersonExistsEx());
            }
        }
        Person person = new Person();
        person.setId(id);
        person.setAge(age);
        person.setFirstName(name);
        personList.add(person);
        return person;
    }

    @Override
    public boolean deletePerson(int id) throws PersonNotFoundEx_Exception {
        if (personList.removeIf(p -> p.getId() == id))
            return true;
        throw new PersonNotFoundEx_Exception("Error", new PersonNotFoundEx());
    }

    @Override
    public Person updatePerson(int id, String name, int age) throws PersonNotFoundEx_Exception {
        Person person = personList.stream()
                .filter(p -> p.getId() == id).findAny()
                .orElseThrow(() -> new PersonNotFoundEx_Exception("Error", new PersonNotFoundEx()));
        person.setFirstName(name);
        person.setAge(age);
        return person;
    }

    @Override
    public int countPersons() {
        return personList.size();
    }

    @Override
    public boolean clearPersons() {
        personList.clear();
        if (personList.size() == 0)
            return true;
        return false;
    }
}
