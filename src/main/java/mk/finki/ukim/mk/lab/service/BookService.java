package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Author;
import mk.finki.ukim.mk.lab.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> listBooks();
    Author addAuthorToBook(Long authorId, String isbn);
    Book findBookByIsbn(String isbn);

    List<Book> findBooksByName (String name);

   public void deleteById(Long id);
    public Book findById(Long id);

    void editBook(Long id, String isbn, String title, String genre, Integer year, Long bookStore);

    void addBook(String isbn, String title, String genre, Integer year, Long bookStore);
    public List<Book> findBooks(Long id);
}
