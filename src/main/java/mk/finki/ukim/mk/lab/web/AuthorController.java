package mk.finki.ukim.mk.lab.web;

import mk.finki.ukim.mk.lab.model.Author;
import mk.finki.ukim.mk.lab.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    String listAuthors(@RequestParam String bookIsbn, Model model){
        List<Author> authorList = authorService.listAuthors(bookIsbn);

        model.addAttribute("authors", authorList);
        model.addAttribute("isbn", bookIsbn);

        return "authorList";
    }



}
