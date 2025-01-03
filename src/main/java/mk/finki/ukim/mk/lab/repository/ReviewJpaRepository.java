package mk.finki.ukim.mk.lab.repository;

import mk.finki.ukim.mk.lab.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    List<Review> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
}
