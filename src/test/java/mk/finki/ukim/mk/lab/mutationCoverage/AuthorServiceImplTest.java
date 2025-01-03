package mk.finki.ukim.mk.lab.mutationCoverage;

import mk.finki.ukim.mk.lab.model.Author;
import mk.finki.ukim.mk.lab.repository.AuthorJpaRepository;
import mk.finki.ukim.mk.lab.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class AuthorServiceImplTest {
    @MockBean
    private AuthorJpaRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Test
    void listAuthorsTest() {
        Author author = new Author("Test", "Author", "Bio");
        when(authorRepository.findAllByBooks_IsbnNotOrBooksIsNull("123"))
                .thenReturn(List.of(author));

        List<Author> authors = authorService.listAuthors("123");
        assertEquals(1, authors.size());
        verify(authorRepository).findAllByBooks_IsbnNotOrBooksIsNull("123");
    }

    @Test
    void findByIdTest() {
        Author author = new Author("Test", "Author", "Bio");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author found = authorService.findById(1L);
        assertNotNull(found);
        assertEquals("Test", found.getName());
    }
}
