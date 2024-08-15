package ru.libraryRest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.libraryRest.dto.PersonDTO;
import ru.libraryRest.models.Person;
import ru.libraryRest.service.PersonService;
import ru.libraryRest.util.JWTUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class PersonController {
    private final PersonService personService;
    private final JWTUtil jwtUtil;

    @Autowired
    public PersonController(PersonService personService, JWTUtil jwtUtil) {
        this.personService = personService;
        this.jwtUtil = jwtUtil;
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

    @PutMapping("/edit/{id}")
    public void editPerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        personService.editPerson(id, personDTO);
    }

    @PostMapping("/create")
    public PersonDTO createPerson(@RequestHeader(value = "Authorization") String auth, @RequestBody PersonDTO personDTO) {
        String creator = jwtUtil.getUsername(auth);
        return personService.createPerson(personDTO, creator);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePerson(@RequestHeader(value = "Authorization") String auth, @PathVariable Long id) {
        String creator = jwtUtil.getUsername(auth);
        personService.deletePerson(id, creator);
    }

    @GetMapping("/restore/{id}")
    public void restorePerson(@PathVariable Long id) {
        personService.restorePerson(id);
    }

}



