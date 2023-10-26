package articles.articles_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository interfaces should not be marked as abstract
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
//add spring-boot-starter-data-jpa to Pom.xml file