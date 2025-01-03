package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    public List<Review> findAll();

    Optional<Review> addReview(Long bookId, Integer score, String description);

    List<Review> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
}
