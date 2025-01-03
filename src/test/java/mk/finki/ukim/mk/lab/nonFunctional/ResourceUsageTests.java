package mk.finki.ukim.mk.lab.nonFunctional;
import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.Review;
import mk.finki.ukim.mk.lab.service.BookService;
import mk.finki.ukim.mk.lab.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResourceUsageTests {
    @Autowired
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;

    @Test
    void bookListingMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // Test memory usage with repeated operations
        for (int i = 0; i < 100; i++) {
            List<Book> books = bookService.listBooks();
            books = null;  // Allow for garbage collection
        }

        System.gc();  // Request garbage collection
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();

        // Check memory usage hasn't grown significantly
        assertTrue((finalMemory - initialMemory) < 5_000_000,
                "Book listing appears to be leaking memory");
    }

    @Test
    void reviewSearchMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        LocalDateTime from = LocalDateTime.now().minusDays(30);
        LocalDateTime to = LocalDateTime.now();

        // Test memory usage with repeated operations
        for (int i = 0; i < 100; i++) {
            List<Review> reviews = reviewService.findByTimestampBetween(from, to);
            reviews = null;  // Allow for garbage collection
        }

        System.gc();  // Request garbage collection
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();

        assertTrue((finalMemory - initialMemory) < 5_000_000,
                "Review search appears to be leaking memory");
    }
}
