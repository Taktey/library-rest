package ru.libraryRest.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.libraryRest.dto.AuthDTO;
import ru.libraryRest.dto.PersonDTO;
import ru.libraryRest.models.Person;
import ru.libraryRest.security.PersonDetails;
import ru.libraryRest.service.PersonService;
import ru.libraryRest.util.JWTUtil;
import ru.libraryRest.validation.PersonValidator;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonService personService;
    private final JWTUtil jwtUtil;
    private final PersonValidator personValidator;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(PersonService personService, JWTUtil jwtUtil, PersonValidator personValidator, AuthenticationManager authenticationManager) {
        this.personService = personService;
        this.jwtUtil = jwtUtil;
        this.personValidator = personValidator;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken userToken =
                new UsernamePasswordAuthenticationToken(
                        authDTO.getName(), authDTO.getPassword()
                );
        try {
            authenticationManager.authenticate(userToken);
        } catch (Exception e) {
            return Map.of("error", "incorrect login or password");
        }
        String token = jwtUtil.generateToken(authDTO.getName());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody @Valid AuthDTO authDTO,
                                            BindingResult bindingResult) {

        Person person = personService.convertFromAuthDTOToPerson(authDTO);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return Map.of("message", "error body");
        }
        personService.save(person);
        String token = jwtUtil.generateToken(person.getName());
        return Map.of("jwt-token", token);
    }
}
