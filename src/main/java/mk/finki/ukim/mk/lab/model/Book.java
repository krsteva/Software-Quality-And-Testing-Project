package mk.finki.ukim.mk.lab.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@Entity
public class Book {
    private String isbn;
    private String title;
    private String genre;
    private int year;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BookStore bookStore;
    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Review> reviews;

    public Book(String isbn, String title, String genre, int year, List<Author> authors, BookStore bookStore) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.authors = authors;
        this.bookStore = bookStore;
    }

    public Book(String isbn, String title, String genre, Integer year, Long bookStore) {

    }
    public double getAverage() {
        if(reviews.isEmpty()){
            return 0.0;
        }
        return (double) reviews.stream().mapToInt(Review::getScore).sum()/reviews.size();
   }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                '}';
    }
}

