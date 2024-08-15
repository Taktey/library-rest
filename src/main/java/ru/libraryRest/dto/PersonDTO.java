package ru.libraryRest.dto;


import lombok.Data;

@Data
public class PersonDTO {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private String phoneNumber;
    private String role;
}
