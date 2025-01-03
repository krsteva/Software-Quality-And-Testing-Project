package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.Review;
import mk.finki.ukim.mk.lab.repository.BookJpaRepository;
import mk.finki.ukim.mk.lab.repository.ReviewJpaRepository;
import mk.finki.ukim.mk.lab.service.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class ReviewServiceImpl implements ReviewService {

    private final ReviewJpaRepository reviewRepository;
    private final BookJpaRepository bookRepository;

    public ReviewServiceImpl(ReviewJpaRepository reviewRepository, BookJpaRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
    @Override
    public Optional<Review> addReview(Long bookId, Integer score, String description) {
        Optional<Book> book = bookRepository.findById(bookId);
        return book.map(value -> Optional.of(reviewRepository.save(new Review(score, description, value)))).orElse(null);
    }

//    @Override
//    public Optional<Review> addReview(Long bookId, Integer score, String description, LocalDateTime localDateTime) {
//        Optional<Book> book = bookRepository.findById(bookId);
//        return book.map(value -> Optional.of(reviewRepository.save(new Review(score, description, value)))).orElse(null);
//    }
    @Override
    public List<Review> findByTimestampBetween(LocalDateTime from, LocalDateTime to) {
        return reviewRepository.findByTimestampBetween(from,to);
    }
}
