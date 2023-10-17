package articles.articles_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//add spring-boot-starter-data-jpa to Pom.xml file
@Repository
public abstract class ArticleRepository implements JpaRepository<Article, Long> {
}