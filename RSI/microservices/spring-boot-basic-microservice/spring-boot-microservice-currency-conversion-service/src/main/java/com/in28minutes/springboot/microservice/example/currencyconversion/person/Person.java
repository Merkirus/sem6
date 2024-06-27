package com.in28minutes.springboot.microservice.example.currencyconversion.person;

import com.in28minutes.springboot.microservice.example.currencyconversion.person.status.PersonStatus;
import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "person")
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer age;
    @ManyToOne
    @JoinColumn(name = "id_person_status", nullable = false, foreignKey = @ForeignKey(name = "fk_person_status"))
    private PersonStatus status;

    public Person() {}

    public Person(Long id, String name, Integer age, PersonStatus status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
    }

    public Person(String name, Integer age, PersonStatus status) {
        this.name = name;
        this.age = age;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(name, person.name) && Objects.equals(age, person.age) && Objects.equals(status.getName(), person.status.getName());
    }
}
