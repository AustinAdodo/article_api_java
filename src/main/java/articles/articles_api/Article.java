package articles.articles_api;

import jakarta.persistence.*;

//JPA (Java Persistence API) -> model
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String title;
    private String body;

    public Article() {
        // Default constructor required by JPA
    }

    public Article(String title) {
        this.title = title;
    }

    public Article(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

// import org.springframework.data.annotation.Id; <---used for MongoDB and KVP databases
// @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
// @GenericGenerator(name = "native", strategy = "native")