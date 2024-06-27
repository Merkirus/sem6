package com.in28minutes.springboot.microservice.example.currencyconversion.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long> {
}
