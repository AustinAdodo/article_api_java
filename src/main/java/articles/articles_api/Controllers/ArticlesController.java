package articles.articles_api.Controllers;

import articles.articles_api.Article;
import articles.articles_api.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticlesController {
    private final ArticleService service;

    @Autowired
    public ArticlesController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("/{articles}")
    public List<Article> Get_All() {
        return this.service.getAll();
    }

    @GetMapping("/{articles}/{id}")
    public ResponseEntity<Article> get(@PathVariable int id) {
        Article result = this.service.findById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/articles")
    public Article create(@RequestBody Article article) {
        return this.service.add(article);
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Void> update(@PathVariable int id) {
        Article result = service.findById(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Article result = this.service.findById(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.service.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
