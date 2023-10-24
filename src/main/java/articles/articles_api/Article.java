package articles.articles_api;

import lombok.Getter;

@Getter
public class Article {
    private final String title;
    private String body;
    private int id;

    public Article(String title) {
        this.title = title;
    }

    public int getId() {
        int Id = id;
        return id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getBody() {
        return this.body;
    }

    public String getTitle() {
        return this.title;
    }
}
