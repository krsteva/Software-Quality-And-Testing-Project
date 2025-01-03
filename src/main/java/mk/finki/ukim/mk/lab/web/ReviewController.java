package mk.finki.ukim.mk.lab.web;

import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.Review;
import mk.finki.ukim.mk.lab.service.BookService;
import mk.finki.ukim.mk.lab.service.ReviewService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final BookService bookService;

    public ReviewController(ReviewService reviewService, BookService bookService) {
        this.reviewService = reviewService;
        this.bookService = bookService;
    }

    @GetMapping("/reviews/{id}")
    public String getBooks(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "add-review";
    }

    @GetMapping("/show/{id}")
    public String showBooks(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "showReviews";
    }

    @PostMapping("/add-review")
    public String addReviewWithTime(@RequestParam Long id,
                                    @RequestParam Integer score,
                                    @RequestParam String description) {
        reviewService.addReview(id, score, description);
        return "redirect:/books";

    }

    @GetMapping("/search")
    public String getReviews(@RequestParam(value = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                             @RequestParam(value = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                             Model model) {
        List<Review> results = reviewService.findByTimestampBetween(from, to);
        model.addAttribute("search", true);
        model.addAttribute("searchReviews", results);
        return "showReviews";
    }
}
