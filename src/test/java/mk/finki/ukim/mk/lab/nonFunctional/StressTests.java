package mk.finki.ukim.mk.lab.nonFunctional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class StressTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldHandleRapidBookEdits() throws Exception {

        for (int i = 0; i < 20; i++) {
            mockMvc.perform(post("/books/add")
                            .param("id", "1")
                            .param("isbn", "TEST-ISBN")
                            .param("title", "Test Book " + i)
                            .param("genre", "Test Genre")
                            .param("year", "2024")
                            .param("bookStore", "1"))
                    .andExpect(status().is3xxRedirection());
        }
    }

    @Test
    void shouldHandleRapidPageTransitions() throws Exception {
        for (int i = 0; i < 50; i++) {
            mockMvc.perform(get("/books")).andExpect(status().isOk());
            mockMvc.perform(get("/bookstore")).andExpect(status().isOk());
            mockMvc.perform(get("/books/add-form")).andExpect(status().isOk());
        }
    }
}
