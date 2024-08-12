package ru.libraryRest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.libraryRest.models.Book;
import ru.libraryRest.models.Person;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByIsRemoved(boolean isRemoved);
    List<Book> findByAuthorAndIsRemoved(String author, boolean isRemoved);
    Optional<Book> findByIdAndIsRemoved(Long id, boolean isRemoved);
    List<Book> findByOwnerIdAndIsRemoved(Long personId, boolean isRemoved);
}
