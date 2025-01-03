package mk.finki.ukim.mk.lab.service.impl;

import mk.finki.ukim.mk.lab.model.Author;
import mk.finki.ukim.mk.lab.repository.AuthorJpaRepository;
import mk.finki.ukim.mk.lab.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorJpaRepository authorRepository;

    public AuthorServiceImpl(AuthorJpaRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> listAuthors(String bookIsbn) {
        return authorRepository.findAllByBooks_IsbnNotOrBooksIsNull(bookIsbn);
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }
}
