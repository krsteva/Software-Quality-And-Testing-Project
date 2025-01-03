package mk.finki.ukim.mk.lab.web;

import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bookdetails")
public class BookDetailsController {

    private final BookService bookService;

    public BookDetailsController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    String details(@RequestParam String isbn,
                   @RequestParam Long authorId, Model model){
        Book book = bookService.findBookByIsbn(isbn);
        bookService.addAuthorToBook(authorId, isbn);
        model.addAttribute("book", book);

        return "bookDetails";

    }

}
