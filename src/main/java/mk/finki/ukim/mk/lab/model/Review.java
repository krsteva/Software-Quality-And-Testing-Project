package mk.finki.ukim.mk.lab.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer score;
    private String description;
    @ManyToOne
    private Book book;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public Review(Integer score, String description, Book book) {
        this.score = score;
        this.description = description;
        this.book = book;
        this.timestamp = LocalDateTime.now();
    }

    public Review() {

    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", score=" + score +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
