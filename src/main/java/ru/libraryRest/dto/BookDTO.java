package ru.libraryRest.dto;

import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String name;
    private Integer yearOfProduction;
    private String author;
    private String annotation;
}
