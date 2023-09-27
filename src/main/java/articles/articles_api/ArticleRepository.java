package articles.articles_api;

import org.springframework.data.jpa.repository.JpaRepository;

//add spring-boot-starter-data-jpa to Pom.xml file
public abstract class ArticleRepository implements JpaRepository<Article, Long> {
}