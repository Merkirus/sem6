package com.example.restservice.person;

public class Person {
    private int id;
    private String name;
    private int age;
    private PersonStatus status;

    public Person() {

    }

    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = PersonStatus.ACTIVE;
    }

    public Person(int id, String name, int age, String status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = PersonStatus.valueOf(status);
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }
}
