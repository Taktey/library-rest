package ru.libraryRest.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.libraryRest.dto.AuthDTO;
import ru.libraryRest.dto.PersonDTO;
import ru.libraryRest.exceptions.PersonNotFoundException;
import ru.libraryRest.models.Person;
import ru.libraryRest.repositories.PersonRepository;
import ru.libraryRest.util.JWTUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, JWTUtil jwtUtil) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    public void save(Person person) {
        enrich(person);
        personRepository.save(person);
    }

    public PersonDTO convertFromPersonToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public Person convertFromDTOToPerson(PersonDTO personDTO) {
        Person person = modelMapper.map(personDTO, Person.class);
        enrich(person);
        return person;
    }
    public Person convertFromAuthDTOToPerson(AuthDTO authDTO) {
        Person person = modelMapper.map(authDTO, Person.class);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        enrich(person);
        return person;
    }

    private void enrich(Person p) {
        p.setCreatedAt(LocalDateTime.now());
        p.setCreatedPerson ("ADMIN");
        p.setRole("ROLE_USER");
    }

    public Person personById(Long id) {
        Optional<Person> byId = personRepository.findById(id);
        return byId.orElseThrow(() -> new PersonNotFoundException("Person is not found"));
    }

    public List<Person> getAllPeople() {
        return personRepository.findByIsRemoved(false);
    }

    public List<Person> getAllRemovedPeople() {
        return personRepository.findByIsRemoved(true);
    }

    public void editPerson(Long id, PersonDTO personDTO) {
        Person person = convertFromDTOToPerson(personDTO);
        person.setId(id);
        personRepository.save(person);
    }

    public void deletePerson(Long id, String deletedBy) {
        Person person = personById(id);
        person.setIsRemoved(true);
        person.setRemovedPerson(deletedBy);
        person.setRemovedAt(LocalDateTime.now());
        personRepository.save(person);
    }

    public void restorePerson(Long id) {
        Person person = personById(id);
        person.setIsRemoved(false);
        personRepository.save(person);
    }

    public PersonDTO createPerson(PersonDTO personDTO, String creator) {
        Person person = convertFromDTOToPerson(personDTO);
        person.setIsRemoved(false);
        person.setCreatedPerson(creator);
        person.setCreatedAt(LocalDateTime.now());
        personRepository.save(person);
        return convertFromPersonToPersonDTO(person);
    }

    public String getRole(String name) {
        Person person = personRepository.findByNameAndIsRemoved(name, false).orElseThrow(() -> new PersonNotFoundException("Person is not found"));
        return person.getRole();
    }
}
