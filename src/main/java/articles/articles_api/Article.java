package articles.articles_api;

import lombok.Getter;
import lombok.Setter;

public class Article {
    @Getter
    private String title;
    private String Body;
    private int id;

    public Article(String title) {
        this.title = title;
    }

    public String getId() {
        Integer Id = id;
        return Id.toString();
    }
}
