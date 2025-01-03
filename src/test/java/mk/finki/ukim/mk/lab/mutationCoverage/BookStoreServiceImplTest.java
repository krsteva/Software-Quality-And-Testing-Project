package mk.finki.ukim.mk.lab.mutationCoverage;

import mk.finki.ukim.mk.lab.model.BookStore;
import mk.finki.ukim.mk.lab.repository.BookStoreJpaRepository;
import mk.finki.ukim.mk.lab.service.BookStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookStoreServiceImplTest {
    @MockBean
    private BookStoreJpaRepository bookStoreRepository;

    @Autowired
    private BookStoreService bookStoreService;

    @Test
    void findAllTest() {
        BookStore bookStore = new BookStore("Test", "City", "Address");
        when(bookStoreRepository.findAll()).thenReturn(List.of(bookStore));

        List<BookStore> result = bookStoreService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void editTest() {
        BookStore bookStore = new BookStore("Old", "City", "Address");
        when(bookStoreRepository.findById(1L)).thenReturn(Optional.of(bookStore));
        when(bookStoreRepository.save(any())).thenAnswer(invocation -> {
            BookStore savedBookStore = invocation.getArgument(0);
            return savedBookStore;
        });

        BookStore edited = bookStoreService.edit(1L, "New", "NewCity", "NewAddress");
        assertEquals("New", edited.getName());
        assertEquals("NewCity", edited.getCity());
        assertEquals("NewAddress", edited.getAddress());
    }
}
