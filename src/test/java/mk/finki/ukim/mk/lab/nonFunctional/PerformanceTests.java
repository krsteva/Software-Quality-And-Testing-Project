package mk.finki.ukim.mk.lab.nonFunctional;

import mk.finki.ukim.mk.lab.service.BookService;
import mk.finki.ukim.mk.lab.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PerformanceTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;

    @Test
    void listBooks_ShouldRespondQuickly() throws Exception {
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(duration < 1000,
                "Book listing took " + duration + "ms, which exceeds the 1-second threshold");
    }

    @Test
    void bookStore_DetailsShouldLoadQuickly() throws Exception {
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/bookstore/details/1"))
                .andExpect(status().isOk());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(duration < 1000,
                "BookStore details loading took " + duration + "ms, which exceeds the 1-second threshold");
    }

    @Test
    void reviews_SearchShouldBeEfficient() throws Exception {
        LocalDateTime from = LocalDateTime.now().minusDays(30);
        LocalDateTime to = LocalDateTime.now();

        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/review/search")
                        .param("from", from.toString())
                        .param("to", to.toString()))
                .andExpect(status().isOk());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(duration < 1000,
                "Review search took " + duration + "ms, which exceeds the 1-second threshold");
    }
}