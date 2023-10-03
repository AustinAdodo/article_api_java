package articles.articles_api;

import lombok.Getter;

public class Article {
    @Getter
    private String title;
    private String Body;
    private int id;

    public Article(String title) {
        this.title = title;
    }

    public String getId() {
        int Id = id;
        return Integer.toString(Id);
    }
}
