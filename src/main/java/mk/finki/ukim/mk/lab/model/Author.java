package mk.finki.ukim.mk.lab.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String biography;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "author_books",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books;

    public Author(String name, String lastName, String biography) {
        this.name = name;
        this.lastName = lastName;
        this.biography = biography;
        this.dateOfBirth = LocalDate.now();
        this.books = new ArrayList<>();
    }

    public Author() {

    }

}
