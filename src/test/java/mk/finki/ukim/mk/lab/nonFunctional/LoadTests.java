package mk.finki.ukim.mk.lab.nonFunctional;
import mk.finki.ukim.mk.lab.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
class LoadTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Test
    void shouldHandleMultipleSimultaneousBookListings() throws Exception {
        int numberOfRequests = 50;
        CountDownLatch latch = new CountDownLatch(numberOfRequests);
        AtomicInteger successCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < numberOfRequests; i++) {
            executor.submit(() -> {
                try {
                    mockMvc.perform(get("/books"))
                            .andExpect(status().isOk());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("Request failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        assertTrue(successCount.get() >= numberOfRequests * 0.95,
                "Less than 95% of concurrent book listing requests succeeded");
    }

    @Test
    void shouldHandleMultipleSimultaneousReviews() throws Exception {
        int numberOfRequests = 20;
        CountDownLatch latch = new CountDownLatch(numberOfRequests);
        AtomicInteger successCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < numberOfRequests; i++) {
            final int score = (i % 5) + 1;
            executor.submit(() -> {
                try {
                    mockMvc.perform(post("/review/add-review")
                                    .param("id", "1")
                                    .param("score", String.valueOf(score))
                                    .param("description", "Load test review"))
                            .andExpect(status().is3xxRedirection());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("Review submission failed: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        assertTrue(successCount.get() >= numberOfRequests * 0.95,
                "Less than 95% of concurrent review submissions succeeded");
    }
}
