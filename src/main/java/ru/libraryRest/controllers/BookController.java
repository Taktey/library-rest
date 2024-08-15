package ru.libraryRest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.libraryRest.dto.BookDTO;
import ru.libraryRest.service.BookService;
import ru.libraryRest.util.JWTUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;
    private final JWTUtil jwtUtil;

    @Autowired
    public BookController(BookService bookService, JWTUtil jwtUtil) {
        this.bookService = bookService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/all")
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/cover/{id}")
    public ResponseEntity<byte[]> getCoverById(@PathVariable Long id) throws IOException {
        String pathAsString = bookService.getCover(id);
        Path path = Paths.get(pathAsString);

        return new ResponseEntity<>(Files.readAllBytes(path), HttpStatus.OK);
    }

    @GetMapping("/author/{author}")
    public List<BookDTO> getBooksByAuthor(@PathVariable String author) {
        return bookService.getBooksByAuthor(author);
    }

    @PostMapping("/add")
    public void addBook(@RequestBody BookDTO bookDTO, @RequestHeader(value = "Authorization") String auth) {
        String creator = jwtUtil.getUsername(auth);
        bookService.addBook(bookDTO, creator);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBook(@PathVariable Long id, @RequestHeader(value = "Authorization") String auth) {
        String deletedBy = jwtUtil.getUsername(auth);
        bookService.deleteBook(id, deletedBy);
    }

    @GetMapping("/restore/{id}")
    public void restoreBook(@PathVariable Long id, @RequestHeader(value = "Authorization") String auth) {
        String restoredBy = jwtUtil.getUsername(auth);
        bookService.restoreBook(id, restoredBy);
    }

    @PutMapping("/update/{id}")
    public void updateBook(@RequestBody BookDTO bookDTO, @RequestHeader(value = "Authorization") String auth) {
        String updatedBy = jwtUtil.getUsername(auth);
        bookService.updateBook(bookDTO, updatedBy);
    }

    @PostMapping("/give/{bookId}/to/{personId}")
    public void giveBookToPerson(@PathVariable Long bookId,
                                 @PathVariable Long personId,
                                 @RequestHeader(value = "Authorization") String auth) {
        String assignedBy = jwtUtil.getUsername(auth);
        bookService.giveBookToPerson(bookId, personId, assignedBy);
    }

    @GetMapping("/show/{personId}")
    public List<BookDTO> showBooksByOwner(@PathVariable Long personId) {
        return bookService.showBooksByOwner(personId);
    }

    @GetMapping("/tome/{id}")
    public void giveBookToMe(@PathVariable Long id, @RequestHeader(value = "Authorization") String auth) {
        String askedBy = jwtUtil.getUsername(auth);
        bookService.giveBookToMe(id, askedBy);
    }

    @GetMapping("/release/{bookId}")
    public void releaseBook(@PathVariable Long bookId, @RequestHeader(value = "Authorization") String auth) {
        String releasedBy = jwtUtil.getUsername(auth);
        bookService.releaseBook(bookId, releasedBy);
    }
}
