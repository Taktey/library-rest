package ru.libraryRest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.libraryRest.dto.BookDTO;
import ru.libraryRest.service.BookService;

import java.util.List;

@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public List<BookDTO> getAllBooks(){
        return bookService.getAllBooks();
    }
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id){
        return bookService.getBookById(id);
    }
    @GetMapping("/author/{author}")
    public List<BookDTO> getBooksByAuthor(@PathVariable String author){
        return bookService.getBooksByAuthor(author);
    }
    @PostMapping("/add")
    public void addBook(@RequestBody BookDTO bookDTO){
        bookService.addBook(bookDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }
    @GetMapping("/restore/{id}")
    public void restoreBook(@PathVariable Long id){
        bookService.restoreBook(id);
    }
    @PutMapping("/update/{id}")
    public void updateBook(BookDTO bookDTO){
        bookService.updateBook(bookDTO);
    }
    @PostMapping("/give/{bookId}/to/{personId}")
    public void giveBookToPerson(@PathVariable Long bookId,
                                 @PathVariable Long personId){
        bookService.giveBookToPerson(bookId, personId);
    }
    @GetMapping("/show/{personId}")
    public List<BookDTO> showBooksByOwner(@PathVariable Long personId){
        return bookService.showBooksByOwner(personId);
    }
    public void giveBookToMe(Long bookId){}

    @GetMapping("/release/{bookId}")
    public void releaseBook(@PathVariable Long bookId){
        bookService.releaseBook(bookId);
    }




}
