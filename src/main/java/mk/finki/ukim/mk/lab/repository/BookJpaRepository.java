package mk.finki.ukim.mk.lab.repository;

import mk.finki.ukim.mk.lab.model.Author;
import mk.finki.ukim.mk.lab.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Long> {

    Book findBookByIsbn(String isbn);
    List<Book> findAllByBookStoreId(Long bookStoreId);
    List<Book> findByTitle(String title);
}
