package mk.finki.ukim.mk.lab.repository;

import mk.finki.ukim.mk.lab.model.BookStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStoreJpaRepository extends JpaRepository<BookStore, Long> {

}
