package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.Author;


import java.util.List;


public interface AuthorService {
    List<Author> listAuthors(String bookIsbn);
    Author findById(Long id);
}

