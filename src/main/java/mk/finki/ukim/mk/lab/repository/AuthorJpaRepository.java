package mk.finki.ukim.mk.lab.repository;

import mk.finki.ukim.mk.lab.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
    List<Author> findAllByBooks_IsbnNotOrBooksIsNull(String bookId);
}
