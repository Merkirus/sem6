package com.example.restservice.database;

import com.example.restservice.person.Person;
import com.example.restservice.exception.PersonExistsEx;
import com.example.restservice.exception.PersonNotFoundEx;
import com.example.restservice.person.PersonStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService implements IDatabaseService {
    private static List<Person> personsRepo = new ArrayList<>();

    static {
        personsRepo.add(new Person(1, "Rafal", 12));
        personsRepo.add(new Person(2, "Marek", 17));
        personsRepo.add(new Person(3, "Marcin", 15));
    }

    @Override
    public List<Person> getAllPersons() {
        return personsRepo;
    }

    @Override
    public Person getPerson(int id) throws PersonNotFoundEx {
        for (Person thePerson : personsRepo) {
            if (thePerson.getId() == id)
                return thePerson;
        }
        throw new PersonNotFoundEx(id);
    }

    @Override
    public Person updatePerson(Person person) throws PersonNotFoundEx {
        int id = person.getId();
        String name = person.getName();
        int age = person.getAge();
        PersonStatus status = person.getStatus();
        Person _person = personsRepo.stream()
                .filter(p -> p.getId() == id).findAny()
                .orElseThrow(() -> new PersonNotFoundEx(id));
        _person.setAge(age);
        _person.setName(name);
        _person.setStatus(status);
        return _person;
    }

    @Override
    public boolean deletePerson(int id) throws PersonNotFoundEx {
        if (personsRepo.removeIf(p -> p.getId() == id))
            return true;
        throw new PersonNotFoundEx(id);
    }

    @Override
    public Person addPerson(Person person) throws PersonExistsEx {
        int id = person.getId();

        for (Person thePerson : personsRepo) {
            if (thePerson.getId() == id) {
                throw new PersonExistsEx(id);
            }
        }

        personsRepo.add(person);
        return person;
    }
}
