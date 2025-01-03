package mk.finki.ukim.mk.lab.mutationCoverage;

import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.Review;
import mk.finki.ukim.mk.lab.repository.BookJpaRepository;
import mk.finki.ukim.mk.lab.repository.ReviewJpaRepository;
import mk.finki.ukim.mk.lab.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReviewServiceImplTest {
    @MockBean
    private ReviewJpaRepository reviewRepository;
    @MockBean
    private BookJpaRepository bookRepository;

    @Autowired
    private ReviewService reviewService;


    @Test
    void listReviewsTest() {
        Book book = new Book("123", "Test", "Genre", 2024, null);
        Review review = new Review(5, "desc", book);
        when(reviewRepository.findAll()).thenReturn(List.of(review));

        assertEquals(1, reviewService.findAll().size());
    }

    @Test
    void addReviewTest() {
        Book book = new Book("123", "Test", "Genre", 2024, null);
        Review review = new Review(5, "Great", book);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(reviewRepository.save(any())).thenReturn(review);

        Optional<Review> result = reviewService.addReview(1L, 5, "Great");
        assertTrue(result.isPresent());
        assertEquals(5, result.get().getScore());
    }

    @Test
    void findByTimestampBetweenTest() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        Review review = new Review(5, "Great", null);

        when(reviewRepository.findByTimestampBetween(from, to))
                .thenReturn(List.of(review));

        List<Review> results = reviewService.findByTimestampBetween(from, to);
        assertEquals(1, results.size());
    }
}