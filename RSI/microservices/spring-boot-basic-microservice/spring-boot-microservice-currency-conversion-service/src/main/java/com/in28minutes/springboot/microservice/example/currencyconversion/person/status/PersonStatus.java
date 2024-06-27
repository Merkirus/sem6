package com.in28minutes.springboot.microservice.example.currencyconversion.person.status;

import jakarta.persistence.*;

@Entity(name = "person_status")
@Table(name = "person_statuses")
public class PersonStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    public PersonStatus() {}

    public PersonStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PersonStatus(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
