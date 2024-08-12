package ru.libraryRest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.libraryRest.models.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByName(String username);
    Person findByIdAndIsRemoved(Long id, boolean isRemoved);
    List<Person> findByIsRemoved(boolean isRemoved);
}
