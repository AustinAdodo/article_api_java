package articles.articles_api;

//package articles;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
    private static List<Article> articles = new ArrayList<Article>();
    private static ArticleService service = new ArticleService();

    @Autowired
    private MockMvc mockMvc;

    @BeforeClass
    public static void populateArticles() {
        articles.add(new Article("10 things that you thought were unhealthy"));
        articles.add(new Article("You won't sleep until you read this"));
        articles.add(new Article("I ran out of catchy titles"));
    }

    @Before
    public void clearDB() {
        this.service.clear();
    }

    public void addArticles() {
        for (Article article : articles) {
            this.service.add(article);
        }
    }

    @Test
    public void shouldLetUsPostArticles() throws Exception {
        for (Article article : articles) {
            this.mockMvc.perform(post("/articles")
                            .content(asJsonString(article))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("title", is(article.getTitle())));
        }
    }

    @Test
    public void shouldAllowUpdatingArticles() throws Exception {
        addArticles();
        String body = "This is some filler text for a killer article";
        Article article = this.service.getAll().get(0);
        Integer id = (Integer) article.getId();
        if (id == null) {
            article.setId(0);
        }

        article.setBody(body);
        this.mockMvc.perform(put("/articles/" + article.getId())
                        .content(asJsonString(article))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Article actual = this.service.findById(article.getId());
        assertEquals("Should have updated the article", actual.getBody(), body);
    }

    @Test
    public void shouldAllowUsToRemoveArticles() throws Exception {
        addArticles();
        List<Article> all = new ArrayList<Article>(this.service.getAll());
        for (Article article : all) {
            Integer id = (Integer) article.getId();
            if (id == null) {
                article.setId(0);
            }

            this.mockMvc.perform(delete("/articles/" + article.getId()))
                    .andExpect(status().isNoContent());
        }
        assertEquals("Should remove all articles", 0, this.service.getAll().size());
    }
}


//    public static String asJsonString(final Object obj) {
//        try {
//            final ObjectMapper mapper = new ObjectMapper();
//            final String jsonContent = mapper.writeValueAsString(obj);
//            return jsonContent;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

