package com.in28minutes.springboot.microservice.example.currencyconversion.person.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonStatusRepository extends JpaRepository<PersonStatus, Long> {
}
