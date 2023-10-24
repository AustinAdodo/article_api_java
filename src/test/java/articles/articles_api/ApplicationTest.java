package articles.articles_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
    private static final List<Article> articles = new ArrayList<Article>();
    private static final ArticleService service = new ArticleService();

    @Autowired
    /**
     * Injects the mock Library.
     */
    private MockMvc mockMvc;

    @BeforeClass
    public static void populateArticles() {
        articles.add(new Article("10 things that you thought were unhealthy"));
        articles.add(new Article("You won't sleep until you read this"));
        articles.add(new Article("I ran out of catchy titles"));
    }

    @Before
    public void clearDB() {
        service.clear();
    }

    public void addArticles() {
        for (Article article : articles) {
            service.add(article);
        }
    }

    /**
     * Test showing articles can be removed.
     *
     * @param {this.mockMvc} injected dependency from mock library.
     * @param {article}      article instance.
     * @return 200 OK, JSON_UTF8.
     */
    @Test
    public void shouldLetUsPostArticles() throws Exception {
        for (Article article : articles) {
            this.mockMvc.perform(post("/articles")
                            .content(asJsonString(article))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) //TestUtil.APPLICATION_JSON_UTF8
                    .andExpect(jsonPath("title", is(article.getTitle())));
        }
    }

    @Test
    public void shouldAllowUpdatingArticles() throws Exception {
        addArticles();
        String body = "This is some filler text for a killer article";
        Article article = service.getAll().get(0);
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
        Article actual = service.findById(article.getId()); //getId returns int.
        assertEquals("Should have updated the article", actual.getBody(), body);
    }

    @Test
    public void shouldAllowUsToRemoveArticles() throws Exception {
        addArticles();
        List<Article> all = new ArrayList<Article>(service.getAll());
        for (Article article : all) {
            Integer id = (Integer) article.getId();
            if (id == null) {
                article.setId(0);
            }
            this.mockMvc.perform(delete("/articles/" + article.getId()))
                    .andExpect(status().isNoContent());
        }
        assertEquals("Should remove all articles", 0, service.getAll().size());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}





