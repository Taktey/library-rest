package ru.libraryRest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.libraryRest.dto.PersonDTO;
import ru.libraryRest.models.Person;
import ru.libraryRest.service.PersonService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/active")
    public List<PersonDTO> getAllPeople() {
        List<Person> allPeople = personService.getAllPeople();
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person p : allPeople) {
            personDTOList.add(personService.convertFromPersonToPersonDTO(p));
        }
        return personDTOList;
    }

    @GetMapping("/removed")
    public List<PersonDTO> getRemovedPeople() {
        List<Person> allPeople = personService.getAllRemovedPeople();
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person p : allPeople) {
            personDTOList.add(personService.convertFromPersonToPersonDTO(p));
        }
        return personDTOList;
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable Long id) {
        Person byId = personService.personById(id);
        return personService.convertFromPersonToPersonDTO(byId);
    }

}



