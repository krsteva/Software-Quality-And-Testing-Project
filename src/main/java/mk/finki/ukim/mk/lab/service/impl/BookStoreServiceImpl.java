package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.model.BookStore;
import mk.finki.ukim.mk.lab.repository.BookStoreJpaRepository;
import mk.finki.ukim.mk.lab.service.BookStoreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class BookStoreServiceImpl implements BookStoreService {

    private final BookStoreJpaRepository bookStoreRepository;

    public BookStoreServiceImpl(BookStoreJpaRepository bookStoreRepository) {
        this.bookStoreRepository = bookStoreRepository;
    }

    @Override
    public List<BookStore> findAll() {
        return bookStoreRepository.findAll();
    }

    public Optional<BookStore> findById(Long id) {
        return bookStoreRepository.findById(id);
    }

    @Override
    public BookStore edit(Long id, String name, String city, String address) {
        BookStore bookStore = new BookStore();
            bookStore = findById(id).orElse(null);
        
        bookStore.setAddress(address);
        bookStore.setCity(city);
        bookStore.setName(name);
        return bookStoreRepository.save(bookStore);
    }
}
