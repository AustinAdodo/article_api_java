package articles.articles_api;

import jakarta.transaction.Transactional;
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

    public ArticleService() {}

    @Transactional
    public List<Article> getAll() {
        List<Article> all = new ArrayList<>();
        if (this.repo != null) { all = repo.findAll();}
        if (!all.isEmpty()) {return all;}
        List<Article> emptyList = new ArrayList<>();
        return emptyList;
    }

    @Transactional
    public Article findById(int id) {
        Integer Id = id;
        return repo.findById(id).get();
    }

    @Transactional
    public void remove(int id) {
        try {
            Article article = findById(id);
            repo.delete(article);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public Article add(Article article) {
//        Article current_article =  findById(article.id);
        if (article != null && this.repo!=null) {
            repo.save(article);
            return article;
        }
        return new Article();
    }

    @Transactional
    public void UpdateArticle(Article article) {
        if (this.repo != null) {
            repo.save(article);
        }
    }

    @Transactional
    public void clear() {
        if (this.repo != null) {
            this.repo.flush();
        }
    }

    public void deleteAll() {
    }

    public void saveAll(List<Article> articles) {
    }
}
