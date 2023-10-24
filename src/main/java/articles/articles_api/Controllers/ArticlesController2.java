package articles.articles_api.Controllers;

import articles.articles_api.Article;
import articles.articles_api.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class ArticlesController2 {
    private final ArticleService service;
    @Autowired
    public ArticlesController2(ArticleService service) {
        this.service = service;
    }

    @GetMapping("/{articles}")
    public List<Article> getAll() {
        List<Article> articles = this.service.getAll();
        return articles != null ? articles : Collections.emptyList();
    }

    @GetMapping("/{articles}/{id}")
    public ResponseEntity<Article> get(@RequestParam("id") int id) {
        Article result = this.service.findById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

}
