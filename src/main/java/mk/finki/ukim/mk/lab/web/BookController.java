package mk.finki.ukim.mk.lab.web;

import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.BookStore;
import mk.finki.ukim.mk.lab.service.BookService;
import mk.finki.ukim.mk.lab.service.BookStoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final BookStoreService bookStoreService;

    public BookController(BookService bookService, BookStoreService bookStoreService) {
        this.bookService = bookService;
        this.bookStoreService = bookStoreService;
    }

    @GetMapping
    public String getBooksPage(@RequestParam(required = false) String error, Model model){
        System.out.println(error);
        if (error != null){
            model.addAttribute("error", " ID Not Found");
        }
        List<Book> books = bookService.listBooks();
        model.addAttribute("books", bookService.listBooks());

        double highestScore =  bookService.listBooks().stream().mapToDouble(book -> book.getAverage()).max().orElse(0.0);
        Book highestRated = bookService.listBooks().stream().filter(book -> book.getAverage()==highestScore).findFirst().orElse(null);
        model.addAttribute("books", bookService.listBooks());
        model.addAttribute("highestRatedBook",highestRated);
        return "listBooks";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id){
        bookService.deleteById(id);
        return "redirect:/books";
    }


    @GetMapping("/edit-form/{id}")
    public String editBook(@PathVariable Long id, Model model){
//        System.out.println(id);
        Book book = bookService.findById(id);
        if (book == null){
            return "redirect:/books?error=true";
        }
        List<BookStore> bookStore = bookStoreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("bookStores", bookStore);
        return "add-book";
    }


    @GetMapping("/add-form")
    public String getAddBookForm(Model model){
        List<BookStore> bookStore = bookStoreService.findAll();
        model.addAttribute("bookStores", bookStore);
        return "add-book";
    }


    @PostMapping("/add")
    public String saveBook(@RequestParam String isbn,
                           @RequestParam String title,
                           @RequestParam String genre,
                           @RequestParam Integer year,
                           @RequestParam(required = false) Long id,
                           @RequestParam Long bookStore) {
        bookService.editBook(id, isbn, title, genre, year, bookStore);
        return "redirect:/books";
    }




}
