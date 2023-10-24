package articles.articles_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "articles.articles_api")
public class ArticlesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticlesApiApplication.class, args);
    }
}
