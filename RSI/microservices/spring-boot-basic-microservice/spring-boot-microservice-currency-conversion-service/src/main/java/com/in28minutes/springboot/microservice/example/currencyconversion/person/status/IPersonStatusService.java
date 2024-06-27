package com.in28minutes.springboot.microservice.example.currencyconversion.person.status;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface IPersonStatusService {
    @Transactional
    public abstract PersonStatus addPersonStatus(PersonStatus personStatus);
    public abstract Collection<PersonStatus> getPersonStatuses();
    public abstract PersonStatus getPersonStatus(Long id);
    @Transactional
    public abstract PersonStatus updatePersonStatus(Long id, PersonStatus personStatus);
    @Transactional
    public abstract Boolean deletePersonStatus(Long id);
}
