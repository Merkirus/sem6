package org.example;

import jakarta.jws.WebService;

import java.util.List;

@WebService(serviceName = "PersonService",
        endpointInterface = "org.example.PersonService")
public class PersonServiceImpl2 implements PersonService {
    private PersonRepository2 dataRepository = new PersonRepositoryImpl2();

    @Override
    public List<Person> getAllPersons() {
        System.out.println("...called getAllPersons");
        return dataRepository.getAllPerson();
    }

    @Override
    public Person getPerson(int id) throws PersonNotFoundEx_Exception {
        System.out.println("...called getPerson");
        try {
            return dataRepository.getPerson(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new PersonNotFoundEx_Exception("Error", new PersonNotFoundEx());
    }

    @Override
    public Person addPerson(int id, String name, int age) throws PersonExistsEx_Exception {
        System.out.println("...called addPerson");
        try {
            return dataRepository.addPerson(id, name, age);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new PersonExistsEx_Exception("Error", new PersonExistsEx());
    }

    @Override
    public boolean deletePerson(int id) throws PersonNotFoundEx_Exception {
        System.out.println("...called deletePerson");
        try {
            return dataRepository.deletePerson(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new PersonNotFoundEx_Exception("Error", new PersonNotFoundEx());
    }

    @Override
    public Person updatePerson(int id, String name, int age) throws PersonNotFoundEx_Exception {
        System.out.println("...called updatePerson");
        try {
            return dataRepository.updatePerson(id, name, age);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        throw new PersonNotFoundEx_Exception("Error", new PersonNotFoundEx());
    }

    @Override
    public int countPersons() {
        return dataRepository.countPersons();
    }

    @Override
    public boolean clearPersons() {
        return dataRepository.clearPersons();
    }
}
