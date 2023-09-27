package articles.articles_api.Controllers;

import articles.articles_api.Article;
import articles.articles_api.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticlesController {
    private final ArticleService service;
    @Autowired
    public ArticlesController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("/{articles}")
    public List<Article> Get_All(){
        return this.service.getAll();
    }

    @GetMapping("/{articles}/{id}")
    public Article Get(@PathVariable int id){
        Article result = this.service.findById(id);
        if (result == null){return null; }
        return result;
    }

}
