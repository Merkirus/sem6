package com.in28minutes.springboot.microservice.example.currencyconversion.person;

import com.in28minutes.springboot.microservice.example.currencyconversion.CurrencyExchangeServiceProxy;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.exception.BadRequestEx;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.exception.ConflictEx;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.exception.PersonExistsEx;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.exception.PersonNotFoundEx;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.status.IPersonStatusRepository;
import com.in28minutes.springboot.microservice.example.currencyconversion.person.status.PersonStatus;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService implements IPersonService {
    private final CurrencyExchangeServiceProxy supportServiceClient;
    private final IPersonRepository personRepository;
    private final IPersonStatusRepository personStatusRepository;

    public PersonService(IPersonRepository personRepository, IPersonStatusRepository personStatusRepository, CurrencyExchangeServiceProxy supportServiceClient) {
        this.personRepository = personRepository;
        this.personStatusRepository = personStatusRepository;
        this.supportServiceClient = supportServiceClient;
        PersonStatus s1 = new PersonStatus(1L, "ACTIVE");
        PersonStatus s2 = new PersonStatus(2L, "HIRED");
        PersonStatus s3 = new PersonStatus(3L, "NOT_ACTIVE");
        this.personStatusRepository.save(s1);
        this.personStatusRepository.save(s2);
        this.personStatusRepository.save(s3);
        Person p1 = new Person(1L, "Rafal", 12, personStatusRepository.findById(1L).get());
        Person p2 = new Person(2L, "Marek", 22, personStatusRepository.findById(2L).get());
        Person p3 = new Person(3L, "Marcin", 17, personStatusRepository.findById(3L).get());
        this.personRepository.save(p1);
        this.personRepository.save(p2);
        this.personRepository.save(p3);
    }

    @Override
    public CollectionModel<EntityModel<Person>> getPersons() {
        List<EntityModel<Person>> _list = personRepository.findAll().stream().map(
                person -> {
                    EntityModel<Person> em = EntityModel.of(person);
                    em.add(linkTo(methodOn(PersonController.class).getPerson(person.getId())).withSelfRel());
                    if (person.getStatus().getId() == 1L) {
                        em.add(linkTo(methodOn(PersonController.class).deletePerson(person.getId())).withRel("delete"));
                        em.add(linkTo(methodOn(PersonController.class).hirePerson(person.getId())).withRel("hire"));
                        em.add(linkTo(methodOn(PersonController.class).deactivatePerson(person.getId())).withRel("deactivate"));
                    } else if (person.getStatus().getId() == 2L) {
                        em.add(linkTo(methodOn(PersonController.class).vacatePerson(person.getId())).withRel("vacate"));
                    } else {
                        em.add(linkTo(methodOn(PersonController.class).activatePerson(person.getId())).withRel("activate"));
                    }
                    em.add(linkTo(methodOn(PersonController.class).encryptPerson(person.getId())).withRel("encrypt"));
                    em.add(linkTo(methodOn(PersonController.class).decryptPerson(person.getId())).withRel("decrypt"));
                    em.add(linkTo(methodOn(PersonController.class).getPersons()).withRel("list all"));
                    return em;
                }
        ).collect(Collectors.toList());

        return CollectionModel.of(_list,
                linkTo(methodOn(PersonController.class).getPersons()).withSelfRel()
        );
    }

    @Override
    public EntityModel<Person> getPerson(Long id) throws PersonNotFoundEx {
        Person _person = personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundEx(id)
        );
        EntityModel<Person> em = EntityModel.of(_person);
        em.add(linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel());
        if (_person.getStatus().getId() == 1L) {
            em.add(linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"));
            em.add(linkTo(methodOn(PersonController.class).hirePerson(id)).withRel("hire"));
        } else if (_person.getStatus().getId() == 2L) {
            em.add(linkTo(methodOn(PersonController.class).vacatePerson(id)).withRel("vacate"));
        } else {
            em.add(linkTo(methodOn(PersonController.class).activatePerson(id)).withRel("activate"));
        }
        em.add(linkTo(methodOn(PersonController.class).getPersons()).withRel("list all"));
        return em;
    }

    @Override
    @Transactional
    public EntityModel<Person> addPerson(Person person) throws PersonExistsEx {
        Long _id = person.getId();
        if (_id != null && (personRepository.findAll().stream().anyMatch(p -> Objects.equals(p, person))) || personRepository.findById(_id).isPresent()) {
            throw new PersonExistsEx(_id);
        }
        personRepository.save(person);
        return EntityModel.of(person,
                linkTo(methodOn(PersonController.class).getPerson(_id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).deletePerson(_id)).withRel("delete"),
                linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
        );
    }

    @Override
    @Transactional
    public EntityModel<Person> updatePerson(Long id, Person person) throws PersonNotFoundEx {
        Long _id = person.getId();
        if (!Objects.equals(id, _id)) {
            throw new BadRequestEx("Wrong URL id and json body id - update");
        }
        Person old_person = personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundEx(id)
        );
        old_person.setAge(person.getAge());
        old_person.setName(person.getName());
        old_person.setStatus(person.getStatus());
        personRepository.save(old_person);
        return EntityModel.of(old_person,
                linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
        );
    }

    @Override
    @Transactional
    public EntityModel<Boolean> deletePerson(Long id) throws PersonNotFoundEx {
        if (personRepository.findById(id).isEmpty()) {
            throw new PersonNotFoundEx(id);
        }
        personRepository.deleteById(id);
        return EntityModel.of(personRepository.findById(id).isEmpty());
    }

    @Override
    @Transactional
    public EntityModel<Person> hirePerson(Long id) throws ConflictEx {
        Person _person = personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundEx(id)
        );
        PersonStatus ps = _person.getStatus();
        if (ps == personStatusRepository.findById(1L).get()) {
            _person.setStatus(personStatusRepository.findById(2L).get());
            personRepository.save(_person);
            return EntityModel.of(_person,
                    linkTo(methodOn(PersonController.class).hirePerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).vacatePerson(id)).withRel("vacate"),
                    linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
            );
        }
        throw new ConflictEx(id, ps);
    }

    @Override
    @Transactional
    public EntityModel<Person> vacatePerson(Long id) throws ConflictEx {
        Person _person = personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundEx(id)
        );
        PersonStatus ps = _person.getStatus();
        if (ps == personStatusRepository.findById(2L).get()) {
            _person.setStatus(personStatusRepository.findById(1L).get());
            personRepository.save(_person);
            return EntityModel.of(_person,
                    linkTo(methodOn(PersonController.class).vacatePerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).hirePerson(id)).withRel("hire"),
                    linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                    linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
            );
        }
        throw new ConflictEx(id, ps);
    }

    @Override
    @Transactional
    public EntityModel<Person> deactivatePerson(Long id) throws ConflictEx {
        Person _person = personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundEx(id)
        );
        PersonStatus ps = _person.getStatus();
        if (ps == personStatusRepository.findById(1L).get()) {
            _person.setStatus(personStatusRepository.findById(3L).get());
            personRepository.save(_person);
            return EntityModel.of(_person,
                    linkTo(methodOn(PersonController.class).deactivatePerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).activatePerson(id)).withRel("activate"),
                    linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                    linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
            );
        }
        throw new ConflictEx(id, ps);
    }

    @Override
    @Transactional
    public EntityModel<Person> activatePerson(Long id) throws ConflictEx {
        Person _person = personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundEx(id)
        );
        PersonStatus ps = _person.getStatus();
        if (ps == personStatusRepository.findById(3L).get()) {
            _person.setStatus(personStatusRepository.findById(1L).get());
            personRepository.save(_person);
            return EntityModel.of(_person,
                    linkTo(methodOn(PersonController.class).activatePerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).deactivatePerson(id)).withRel("deactivate"),
                    linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                    linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
            );
        }
        throw new ConflictEx(id, ps);
    }

    @Override
    @Transactional
    public EntityModel<Person> encryptPerson(Long id) {
        Person _person = personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundEx(id)
        );
        String _name = supportServiceClient.encrypt(_person.getName()).getBody();
        _person.setName(_name);
        personRepository.save(_person);
        return EntityModel.of(_person);
    }

    @Override
    @Transactional
    public EntityModel<Person> decryptPerson(Long id) {
        Person _person = personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundEx(id)
        );
        String _name = supportServiceClient.decrypt(_person.getName()).getBody();
        _person.setName(_name);
        personRepository.save(_person);
        return EntityModel.of(_person);
    }
}
