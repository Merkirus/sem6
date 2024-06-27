package com.in28minutes.springboot.microservice.example.currencyconversion.person.status;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;

@Service
public class PersonStatusService implements IPersonStatusService {
    private final IPersonStatusRepository personStatusRepository;

    public PersonStatusService(IPersonStatusRepository personStatusRepository) {
        this.personStatusRepository = personStatusRepository;
    }

    @Override
    @Transactional
    public PersonStatus addPersonStatus(PersonStatus personStatus) {
        Long _id = personStatus.getId();
        if (_id != null && personStatusRepository.findAll().stream().anyMatch(ps -> Objects.equals(ps.getName(), personStatus.getName()))){
            throw new RuntimeException();
        }
        personStatusRepository.save(personStatus);
        return personStatus;
    }

    @Override
    public Collection<PersonStatus> getPersonStatuses() {
        return personStatusRepository.findAll();
    }

    @Override
    public PersonStatus getPersonStatus(Long id) {
        return personStatusRepository.findById(id).orElseThrow(
                () -> new RuntimeException()
        );
    }

    @Override
    @Transactional
    public PersonStatus updatePersonStatus(Long id, PersonStatus personStatus) {
        PersonStatus old_status = personStatusRepository.findById(id).orElseThrow(
                () -> new RuntimeException()
        );
        old_status.setName(personStatus.getName());
        personStatusRepository.save(old_status);
        return old_status;
    }

    @Override
    @Transactional
    public Boolean deletePersonStatus(Long id) {
        if (personStatusRepository.findById(id).isEmpty()) {
            throw new RuntimeException();
        }
        personStatusRepository.deleteById(id);
        return personStatusRepository.findById(id).isEmpty();
    }
}
