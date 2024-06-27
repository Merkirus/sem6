package org.example;

import jakarta.xml.ws.Endpoint;

import java.io.IOException;

public class HostService2 {
    public static void main(String[] args) {
        System.out.println("Web Service PersonService is running ...");
        PersonServiceImpl2 psi = new PersonServiceImpl2();
        Endpoint.publish("http://localhost:8082/personservice", psi);
        System.out.println("Press ENTER to STOP PersonService ...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
