package articles.articles_api;

import lombok.Getter;

@Getter
public class Article {
    private final String title;
    private String Body;
    private int id;

    public Article(String title) {
        this.title = title;
    }

    public String getId() {
        int Id = id;
        return Integer.toString(Id);
    }

    public void setBody(String body) {
        Body = body;
    }
}
