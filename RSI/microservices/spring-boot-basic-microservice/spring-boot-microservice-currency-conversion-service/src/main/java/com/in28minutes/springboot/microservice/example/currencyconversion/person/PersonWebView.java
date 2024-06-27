package com.in28minutes.springboot.microservice.example.currencyconversion.person;

public class PersonWebView {
    private Long id;
    private String name;
    private Integer age;
    private String status;

    public PersonWebView() {}

    public PersonWebView(Long id, String name, Integer age, String status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
    }

    public PersonWebView(String name, Integer age, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
