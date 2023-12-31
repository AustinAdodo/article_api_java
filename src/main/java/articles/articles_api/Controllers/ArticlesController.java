package articles.articles_api.Controllers;

import articles.articles_api.Article;
import articles.articles_api.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;


@RestController
public class ArticlesController {
    private final ArticleService service;

    @Autowired
    public ArticlesController(ArticleService service) {
        this.service = service;
    }

    private boolean resourceExists(int id) {
        Article foundArticle = service.findById(id);
        return foundArticle != null;
    }

    private boolean areAllFieldsNull(Article updatedArticle) throws IllegalAccessException {
        for (Field field : updatedArticle.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(updatedArticle) != null) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllParametersEmpty(HttpHeaders headers, String contentType, String forwarded) {
        return headers.isEmpty() && (contentType == null || contentType.isBlank()) && (forwarded == null || forwarded.isBlank());
    }

    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = this.service.getAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        return new ResponseEntity<>(articles, headers, HttpStatus.OK);
    }

    //public ResponseEntity<Article> get(@RequestParam("id") int id)
    /**
     * Gets a specific article detail.
     *
     * @param id retrieved path variable.
     * @return ResponseEntity.ok.
     */

    @GetMapping("/{articles}/{id}")
    public ResponseEntity<Article> get(@PathVariable("id") int id) {
        //Integer.parseInt(id)
        Article result = this.service.findById(id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> create(@RequestBody Article article) {
        if (article != null) {
            HttpHeaders headers = new HttpHeaders();
            this.service.add(article);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
            return new ResponseEntity<>(article, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(new Article(), HttpStatus.NOT_FOUND);
    }

    /**
     * Updates a specific article with retrieved parameters.
     *
     * @param id retrieved path variable.
     * @param updatedArticle the article details to be updated.
     * @param headers of type HttpHeaders as part of the Http Headers.
     * @param forwarded retrieved path variable as part of the request header.
     * @param contentType retrieved path variable as part of the request header for contentType.
     * @return ResponseEntity<articles>.
     */
    @PutMapping("/articles/{id}")
    public ResponseEntity<Void> update(@RequestBody Article updatedArticle,
                                       @RequestHeader HttpHeaders headers,
                                       @RequestHeader("Content-Type") String contentType,
                                       @RequestHeader(value = "Forwarded", required = false) String forwarded,
                                       @PathVariable("id") int id) {

        if (updatedArticle == null || areAllParametersEmpty(headers, contentType, forwarded)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            this.service.UpdateArticle(updatedArticle);
            if (!resourceExists(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates a specific article with retrieved parameters.
     *
     * @param id retrieved path variable, the id of the article to be deleted.
     * @return ResponseEntity<void>.
     */
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        Article result = this.service.findById(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.service.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

//    private boolean areParametersEmptyOrNull(Article updatedArticle) {
//        return updatedArticle == null ||
//                updatedArticle.getHeaders() == null || updatedArticle.getHeaders().isEmpty() ||
//                updatedArticle.getContentType() == null || updatedArticle.getContentType().isBlank() ||
//                updatedArticle.getBody() == null || updatedArticle.getBody().isBlank();
//    }