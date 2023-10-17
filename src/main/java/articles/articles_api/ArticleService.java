package articles.articles_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository repo;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.repo = articleRepository;
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

    public void deleteAll() {
    }

    public void saveAll(List<Article> articles) {
    }
}
