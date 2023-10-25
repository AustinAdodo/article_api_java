package articles.articles_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repo;

    @Autowired
    public ArticleService(ArticleRepository repo) {
        this.repo = repo;
    }
    public ArticleService() {

    }

    public List<Article> getAll() {
        List<Article> all = repo.findAll();
        if (!all.isEmpty()) {
            return all;
        }
        List<Article> emptyList = new ArrayList<>();
        return emptyList;
    }

    public Article findById(int id) {
        Integer Id = id;
        return repo.findById((long) id).get();
    }

    public void remove(int id) {
        try {
            Article article = findById(id);
            repo.delete(article);
        } catch (Exception e) {
            throw e;
        }
    }

    public Article add(Article article) {
//        Article current_article =  findById(article.id);
        if (article != null) {
            repo.save(article);
        }
        return article;
    }

    public void UpdateArticle(Article article) {
        repo.save(article);
    }

    public void clear() {
        this.repo.flush();
    }

    public void deleteAll() {
    }

    public void saveAll(List<Article> articles) {
    }
}
