package mk.finki.ukim.mk.lab.mutationCoverage;

import mk.finki.ukim.mk.lab.model.Author;
import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.BookStore;
import mk.finki.ukim.mk.lab.repository.AuthorJpaRepository;
import mk.finki.ukim.mk.lab.repository.BookJpaRepository;
import mk.finki.ukim.mk.lab.repository.BookStoreJpaRepository;
import mk.finki.ukim.mk.lab.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceImplTest {
    @MockBean
    private BookJpaRepository bookRepository;
    @MockBean
    private AuthorJpaRepository authorRepository;
    @MockBean
    private BookStoreJpaRepository bookStoreRepository;
    @Autowired
    private BookService bookService;

    @Test
    void listBooksTest() {
        Book book = new Book();
        book.setTitle("Test");
        when(bookRepository.findAll()).thenReturn(List.of(book));
        assertEquals(1, bookService.listBooks().size());
        verify(bookRepository).findAll();
    }

    @Test
    void addAuthorToBookTest() {
        // Arrange
        Author author = new Author("Test", "Author", "Bio");
        Book book = new Book();
        List<Book> books = new ArrayList<>();
        List<Author> authors = new ArrayList<>();

        author.setBooks(books);
        book.setAuthors(authors);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findBookByIsbn("123")).thenReturn(book);

        Author result = bookService.addAuthorToBook(1L, "123");

        assertTrue(result.getBooks().contains(book), "Author's books list should contain the added book");

        assertTrue(book.getAuthors().contains(author), "Book's authors list should contain the added author");

        assertEquals(1, result.getBooks().size(), "Author should have exactly one book after addition");
        assertEquals(book, result.getBooks().get(0), "The book in the Author's list should be the one added");

        assertEquals(1, book.getAuthors().size(), "Book should have exactly one author after addition");
        assertEquals(author, book.getAuthors().get(0), "The author in the Book's list should be the one added");

        verify(authorRepository).save(author);
        verify(bookRepository).save(book);
    }



    @Test
    void findBookByIsbnTest() {
        Book book = new Book();
        book.setIsbn("123");
        when(bookRepository.findBookByIsbn("123")).thenReturn(book);

        Book result = bookService.findBookByIsbn("123");
        assertNotNull(result);
        verify(bookRepository).findBookByIsbn("123");
    }

    @Test
    void findBooksByNameTest() {
        Book book = new Book();
        book.setTitle("Test");
        when(bookRepository.findByTitle("Test")).thenReturn(List.of(book));

        List<Book> results = bookService.findBooksByName("Test");
        assertEquals(1, results.size());
        verify(bookRepository).findByTitle("Test");
    }

    @Test
    void deleteByIdTest() {
        bookService.deleteById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void findByIdTest() {
        Book book = new Book();
        book.setTitle("Test");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book found = bookService.findById(1L);
        assertNotNull(found);
        assertEquals("Test", found.getTitle());
        verify(bookRepository).findById(1L);
    }

    @Test
    void editBookTest() {
        Book book = new Book();
        BookStore bookStore = new BookStore();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookStoreRepository.findById(1L)).thenReturn(Optional.of(bookStore));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> {
            Book savedBook = i.getArgument(0);
            assertEquals("title", savedBook.getTitle());
            assertEquals("isbn", savedBook.getIsbn());
            assertEquals("genre", savedBook.getGenre());
            assertEquals(2024, savedBook.getYear());
            assertEquals(bookStore, savedBook.getBookStore());
            return savedBook;
        });

        bookService.editBook(1L, "isbn", "title", "genre", 2024, 1L);
    }
    @Test
    void addBookTest() {
        BookStore bookStore = new BookStore();
        when(bookStoreRepository.findById(1L)).thenReturn(Optional.of(bookStore));

        bookService.addBook("isbn", "title", "genre", 2024, 1L);

        verify(bookRepository).save(any());
        verify(bookStoreRepository).findById(1L);
    }

    @Test
    void findBooksTest() {
        Book book = new Book();
        when(bookRepository.findAllByBookStoreId(1L)).thenReturn(List.of(book));

        List<Book> results = bookService.findBooks(1L);
        assertEquals(1, results.size());
        verify(bookRepository).findAllByBookStoreId(1L);
    }
}