package ru.libraryRest.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.libraryRest.dto.BookDTO;
import ru.libraryRest.models.Book;
import ru.libraryRest.repositories.BookRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findByIsRemoved(false)
                .stream().map(book -> modelMapper.map(book, BookDTO.class)).toList();
    }

    public List<BookDTO> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorAndIsRemoved(author, false)
                .stream().map(book -> modelMapper.map(book, BookDTO.class)).toList();
    }

    public void addBook(BookDTO bookDTO) {
        Book book = convertFromBookDTOToBook(bookDTO);
        bookRepository.save(book);
    }

    private Book convertFromBookDTOToBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        book.setIsRemoved(false);
        book.setCreatedAt(LocalDateTime.now());
        return book;
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setIsRemoved(true);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    public void restoreBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setIsRemoved(false);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    public void updateBook(BookDTO bookDTO) {
        Book book = bookRepository.findById(bookDTO.getId()).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setName(bookDTO.getName());
        book.setAuthor(bookDTO.getAuthor());
        book.setAnnotation(bookDTO.getAnnotation());
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findByIdAndIsRemoved(id, false).orElseThrow(() -> new RuntimeException("Book not found"));
        return modelMapper.map(book, BookDTO.class);
    }

    public void giveBookToPerson(Long bookId, Long personId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setOwnerId(personId);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    public List<BookDTO> showBooksByOwner(Long personId) {
        return bookRepository.findByOwnerIdAndIsRemoved(personId, false)
                .stream().map(book -> modelMapper.map(book, BookDTO.class)).toList();
    }

    public void releaseBook(Long bookId) {
        Book book = bookRepository.findByIdAndIsRemoved(bookId, false).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setOwnerId(null);
    }
}
