package mk.finki.ukim.mk.lab.mutationCoverage;

import mk.finki.ukim.mk.lab.model.Author;
import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.BookStore;
import mk.finki.ukim.mk.lab.repository.AuthorJpaRepository;
import mk.finki.ukim.mk.lab.repository.BookJpaRepository;
import mk.finki.ukim.mk.lab.repository.BookStoreJpaRepository;
import mk.finki.ukim.mk.lab.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

        Author author = new Author("Test", "Author", "Bio");
        Book book = new Book();
        List<Book> initialBooks = new ArrayList<>();
        List<Author> initialAuthors = new ArrayList<>();

        author.setBooks(initialBooks);
        book.setAuthors(initialAuthors);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findBookByIsbn("123")).thenReturn(book);

        List<Book> originalBooks = new ArrayList<>(author.getBooks());
        List<Author> originalAuthors = new ArrayList<>(book.getAuthors());

        Author result = bookService.addAuthorToBook(1L, "123");

        assertNotEquals(originalBooks, result.getBooks(), "Books list should have been modified");
        assertNotEquals(originalAuthors, book.getAuthors(), "Authors list should have been modified");

        assertTrue(result.getBooks().contains(book), "Author's books list should contain the added book");
        assertTrue(book.getAuthors().contains(author), "Book's authors list should contain the added author");

        assertEquals(1, result.getBooks().size(), "Author should have exactly one book after addition");
        assertEquals(book, result.getBooks().get(0), "The book in the Author's list should be the one added");

        assertEquals(1, book.getAuthors().size(), "Book should have exactly one author after addition");
        assertEquals(author, book.getAuthors().get(0), "The author in the Book's list should be the one added");

        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorCaptor.capture());
        Author savedAuthor = authorCaptor.getValue();
        assertTrue(savedAuthor.getBooks().contains(book), "Saved author should have the book in its list");

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();
        assertTrue(savedBook.getAuthors().contains(author), "Saved book should have the author in its list");
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

        Book existingBook = new Book();
        BookStore bookStore = new BookStore();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookStoreRepository.findById(1L)).thenReturn(Optional.of(bookStore));

        bookService.editBook(1L, "isbn1", "title1", "genre1", 2024, 1L);

        assertEquals("isbn1", existingBook.getIsbn());
        assertEquals("title1", existingBook.getTitle());
        assertEquals("genre1", existingBook.getGenre());
        assertEquals(2024, existingBook.getYear());
        assertEquals(bookStore, existingBook.getBookStore());
        verify(bookRepository).save(existingBook);

        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        when(bookStoreRepository.findById(1L)).thenReturn(Optional.of(bookStore));

        bookService.editBook(2L, "isbn2", "title2", "genre2", 2025, 1L);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, atLeast(2)).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();
        assertEquals("isbn2", savedBook.getIsbn());
        assertEquals("title2", savedBook.getTitle());
        assertEquals("genre2", savedBook.getGenre());
        assertEquals(2025, savedBook.getYear());
        assertEquals(bookStore, savedBook.getBookStore());

        bookService.editBook(null, "isbn3", "title3", "genre3", 2026, 1L);

        verify(bookRepository, never()).findById(null);
        ArgumentCaptor<Book> nullIdBookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, atLeast(3)).save(nullIdBookCaptor.capture());
        Book savedNullIdBook = nullIdBookCaptor.getValue();
        assertEquals("isbn3", savedNullIdBook.getIsbn());
        assertEquals("title3", savedNullIdBook.getTitle());
        assertEquals("genre3", savedNullIdBook.getGenre());
        assertEquals(2026, savedNullIdBook.getYear());
        assertEquals(bookStore, savedNullIdBook.getBookStore());
    }

    @Test
    void editBookWithNonExistentBookStoreTest() {
        when(bookStoreRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bookService.editBook(1L, "isbn", "title", "genre", 2024, 999L)
        );

        assertEquals("BookStore not found", exception.getMessage());
        verify(bookStoreRepository).findById(999L);
        verify(bookRepository, never()).save(any());
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