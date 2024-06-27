package org.example.client;

import org.example.Person;
import org.example.PersonNotFoundEx_Exception;
import org.example.PersonService;
import org.example.PersonService_Service;

import java.net.MalformedURLException;
import java.net.URL;

public class PS2Client {
    public static void main(String[] args) throws MalformedURLException, PersonNotFoundEx_Exception {
        int num = -1;
        URL adr = new URL("http://localhost:8082/personservice?wsdl");
        PersonService_Service pService = new PersonService_Service(adr);
        PersonService pServiceProxy = pService.getPersonServiceImplPort();
        num = pServiceProxy.countPersons();
        System.out.println("Num of Persons: " + num);
//        Person person = pServiceProxy.getPerson(2);
//        System.out.println("Person " + person.getFirstName() + ", id " + person.getId());
    }
}
