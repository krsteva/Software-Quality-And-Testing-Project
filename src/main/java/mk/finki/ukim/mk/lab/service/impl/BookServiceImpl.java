package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.model.Author;
import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.BookStore;
import mk.finki.ukim.mk.lab.repository.AuthorJpaRepository;
import mk.finki.ukim.mk.lab.repository.BookJpaRepository;
import mk.finki.ukim.mk.lab.repository.BookStoreJpaRepository;
import mk.finki.ukim.mk.lab.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookJpaRepository bookRepository;
    private final AuthorJpaRepository authorRepository;
    private final BookStoreJpaRepository bookStoreRepository;

    @Autowired
    public BookServiceImpl(BookJpaRepository bookRepository, AuthorJpaRepository authorRepository, BookStoreJpaRepository bookStoreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookStoreRepository = bookStoreRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> listBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Author addAuthorToBook(Long authorId, String isbn) {
        Author author = authorRepository.findById(authorId).orElse(null);
        Book book=bookRepository.findBookByIsbn(isbn);
        List<Book> books = author.getBooks();
        books.add(book);
        author.setBooks(books);
        List<Author> authors = book.getAuthors();
        authors.add(author);
        book.setAuthors(authors);
        bookRepository.save(book);
        authorRepository.save(author);
        return author;
    }

    @Override
    public Book findBookByIsbn(String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

    @Override
    public List<Book> findBooksByName(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book findById(Long id) {
       return bookRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void editBook(Long id, String isbn, String title, String genre, Integer year, Long bookStoreId) {
        Book book = id != null ? bookRepository.findById(id).orElse(new Book()) : new Book();

        BookStore bookStore = bookStoreRepository.findById(bookStoreId)
                .orElseThrow(() -> new RuntimeException("BookStore not found"));

        book.setIsbn(isbn);
        book.setTitle(title);
        book.setGenre(genre);
        book.setYear(year);
        book.setBookStore(bookStore);

        bookRepository.save(book);
    }

    @Override
    public void addBook(String isbn, String title, String genre, Integer year, Long bookStore) {
        BookStore bookStore1 = bookStoreRepository.findById(bookStore).get();
        bookRepository.save(new Book(isbn,title,genre,year,bookStore));
    }

    @Override
    public List<Book> findBooks(Long id){
        return bookRepository.findAllByBookStoreId(id);
    }
}
