package articles.articles_api;

import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Table(name = "articles")
public abstract class ArticleRepository implements JpaRepository<Article, Long> {
}

//add spring-boot-starter-data-jpa to Pom.xml file