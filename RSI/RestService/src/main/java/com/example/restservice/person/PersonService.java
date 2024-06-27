package com.example.restservice.person;

import com.example.restservice.database.IDatabaseService;
import com.example.restservice.exception.BadRequestEx;
import com.example.restservice.exception.ConflictEx;
import com.example.restservice.exception.PersonExistsEx;
import com.example.restservice.exception.PersonNotFoundEx;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService implements IPersonService {
    private final IDatabaseService dbService;

    public PersonService(IDatabaseService dbService) {
        this.dbService = dbService;
    }

    @Override
    public CollectionModel<EntityModel<Person>> getAllPersons() {
        List<EntityModel<Person>> _list = dbService.getAllPersons().stream().map(p -> {
                    if (p.getStatus() == PersonStatus.ACTIVE) {
                        return EntityModel.of(p,
                                linkTo(methodOn(PersonController.class).getPerson(p.getId())).withSelfRel(),
                                linkTo(methodOn(PersonController.class).deletePerson(p.getId())).withRel("delete"),
                                linkTo(methodOn(PersonController.class).hirePerson(p.getId())).withRel("hire"),
                                linkTo(methodOn(PersonController.class).deactivatePerson(p.getId())).withRel("deactivate"),
                                linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
                        );
                    } else if (p.getStatus() == PersonStatus.HIRED) {
                        return EntityModel.of(p,
                                linkTo(methodOn(PersonController.class).getPerson(p.getId())).withSelfRel(),
                                linkTo(methodOn(PersonController.class).vacatePerson(p.getId())).withRel("vacate"),
                                linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
                        );
                    } else {
                        return EntityModel.of(p,
                                linkTo(methodOn(PersonController.class).getPerson(p.getId())).withSelfRel(),
                                linkTo(methodOn(PersonController.class).activatePerson(p.getId())).withRel("activate"),
                                linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
                        );
                    }
                }
        ).collect(Collectors.toList());

        return CollectionModel.of(_list,
                linkTo(methodOn(PersonController.class).getPersons()).withSelfRel()
        );
//        return dbService.getAllPersons();
    }

    @Override
    public EntityModel<Person> getPerson(int id) throws PersonNotFoundEx {
        Person _person = dbService.getPerson(id);
        EntityModel<Person> em = EntityModel.of(_person);
        em.add(linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel());
        if (_person.getStatus() == PersonStatus.ACTIVE) {
            em.add(linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"));
            em.add(linkTo(methodOn(PersonController.class).hirePerson(id)).withRel("hire"));
        }
        if (_person.getStatus() == PersonStatus.HIRED) {
            em.add(linkTo(methodOn(PersonController.class).vacatePerson(id)).withRel("vacate"));
        }
        em.add(linkTo(methodOn(PersonController.class).getPersons()).withRel("list all"));
        return em;
//        return dbService.getPerson(id);
    }

    @Override
    public EntityModel<Person> addPerson(Person person) throws PersonExistsEx {
        Person _person = dbService.addPerson(person);
        int id = _person.getId();
        return EntityModel.of(_person,
                linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
        );
//        return dbService.addPerson(person);
    }

    @Override
    public EntityModel<Person> updatePerson(int id, Person person) throws PersonNotFoundEx {
        int _id = person.getId();
        if (id != _id) {
            throw new BadRequestEx("Wrong url id and json body id - update");
        }
        return EntityModel.of(dbService.updatePerson(person),
                linkTo(methodOn(PersonController.class).getPerson(_id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).deletePerson(_id)).withRel("delete"),
                linkTo(methodOn(PersonController.class).getPersons()).withRel("list all")
        );
//        if (id != person.getId()) {
//            throw new BadRequestEx("Wrong url id and json body id - update");
//        }
//        return dbService.updatePerson(person);
    }

    @Override
    public Boolean deletePerson(int id) throws PersonNotFoundEx {
        return dbService.deletePerson(id);
    }

    @Override
    public EntityModel<Person> hirePerson(int id) throws ConflictEx {
        Person _person = dbService.getPerson(id);
        PersonStatus ps = _person.getStatus();
        if (ps == PersonStatus.ACTIVE) {
            _person.setStatus(PersonStatus.HIRED);
            return EntityModel.of(dbService.updatePerson(_person),
                    linkTo(methodOn(PersonController.class).hirePerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).vacatePerson(id)).withRel("vacate"),
                    linkTo(methodOn(PersonController.class).getPersons()).withRel("list all"));
//            return dbService.updatePerson(_person);
        }
        throw new ConflictEx(id, ps);
    }

    @Override
    public EntityModel<Person> vacatePerson(int id) throws ConflictEx {
        Person _person = dbService.getPerson(id);
        PersonStatus ps = _person.getStatus();
        if (ps == PersonStatus.HIRED) {
            _person.setStatus(PersonStatus.ACTIVE);
            return EntityModel.of(dbService.updatePerson(_person),
                    linkTo(methodOn(PersonController.class).vacatePerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).hirePerson(id)).withRel("hire"),
                    linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                    linkTo(methodOn(PersonController.class).getPersons()).withRel("list all"));
//            return dbService.updatePerson(_person);
        }
        throw new ConflictEx(id, ps);
    }

    @Override
    public EntityModel<Person> deactivatePerson(int id) throws ConflictEx {
        Person _person = dbService.getPerson(id);
        PersonStatus ps = _person.getStatus();
        if (ps == PersonStatus.ACTIVE) {
            _person.setStatus(PersonStatus.NOT_ACTIVE);
            return EntityModel.of(dbService.updatePerson(_person),
                    linkTo(methodOn(PersonController.class).deactivatePerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).activatePerson(id)).withRel("activate"),
                    linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                    linkTo(methodOn(PersonController.class).getPersons()).withRel("list all"));
//            return dbService.updatePerson(_person);
        }
        throw new ConflictEx(id, ps);
    }

    @Override
    public EntityModel<Person> activatePerson(int id) throws ConflictEx {
        Person _person = dbService.getPerson(id);
        PersonStatus ps = _person.getStatus();
        if (ps == PersonStatus.NOT_ACTIVE) {
            _person.setStatus(PersonStatus.ACTIVE);
            return EntityModel.of(dbService.updatePerson(_person),
                    linkTo(methodOn(PersonController.class).activatePerson(id)).withSelfRel(),
                    linkTo(methodOn(PersonController.class).deactivatePerson(id)).withRel("deactivate"),
                    linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                    linkTo(methodOn(PersonController.class).getPersons()).withRel("list all"));
//            return dbService.updatePerson(_person);
        }
        throw new ConflictEx(id, ps);
    }
}
