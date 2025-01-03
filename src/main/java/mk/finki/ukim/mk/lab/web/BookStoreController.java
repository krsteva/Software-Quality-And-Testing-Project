package mk.finki.ukim.mk.lab.web;

import mk.finki.ukim.mk.lab.model.Book;
import mk.finki.ukim.mk.lab.model.BookStore;
import mk.finki.ukim.mk.lab.service.BookService;
import mk.finki.ukim.mk.lab.service.BookStoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/bookstore")
public class BookStoreController {

    private final BookStoreService bookStoreService;
    private final BookService bookService;

    public BookStoreController(BookStoreService bookStoreService, BookService bookService) {
        this.bookStoreService = bookStoreService;
        this.bookService = bookService;
    }

    @GetMapping
    public String getPage(Model model){
        List<BookStore> bookStores = bookStoreService.findAll();
        model.addAttribute("bookstores", bookStores);
        return "bookStore";
    }

    @GetMapping("/details/{id}")
    public String getById(@PathVariable Long id, Model model){
        Optional<BookStore> bookstore = bookStoreService.findById(id);
        System.out.println(bookstore);
        List<Book> books = this.bookService.findBooks(id);
        model.addAttribute("bookstore", bookstore.orElseGet(null));
        model.addAttribute("books", books);
        return "bookStoreDetails";

    }

    @GetMapping("/add-form")
    public String getAddBookStoreForm(){
        return "add-bookstore";
    }

    @GetMapping("/edit-form/{id}")
    public String editBookStore(@PathVariable Long id, Model model){
        BookStore bookStore = bookStoreService.findById(id).orElse(null);
        if (bookStore == null){
            return "redirect:/booksstore?error=true";
        }
        model.addAttribute("bookStores", bookStore);
        return "add-bookstore";
    }


    @PostMapping("/add")
    public String saveBookStore(@RequestParam String name,
                           @RequestParam String city,
                           @RequestParam String address,
                           @RequestParam(required = false) Long id) {
        bookStoreService.edit(id, name, city, address);
        return "redirect:/bookstore";
    }




}
