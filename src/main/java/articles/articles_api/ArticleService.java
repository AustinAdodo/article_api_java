package articles.articles_api;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository repo;
    public ArticleService(ArticleRepository repository) {
        this.repo = repository;
    }
    public List<Article> getAll() {
        return repo.findAll();
    }

    public Article findById(int id) {
        Integer Id = id;
        return repo.findById((long) id).get();
    }

    public void remove(int id) {
        try{
          Article article =  findById(id);
          repo.delete(article);
        }
        catch(Exception e){throw e; }
    }

    public Article add(Article article) {
//        Article current_article =  findById(article.id);
        if(article != null){repo.save(article);}
        return article;
    }

    public void clear() {
        this.repo.flush();
    }
}
