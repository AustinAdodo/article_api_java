package articles.articles_api;

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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test Class ApplicationTest. <br/><br/>
 * Note: To prevent injection problems this class intentionally does not extend RestTemplate
 * Note: If you need customizations (for example to adding additional message converters) use
 * a RestTemplateBuilder @Bean <br/><br/>
 * The library you need to import for the is() matcher is org.hamcrest.Matchers.
 * This library provides a
 * variety of matchers that can be used to assert the truth or falsity of conditions in your tests.
 */

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = {TestContext.class, WebAppContext.class})
//@WebAppConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {
    private static final List<Article> articles = new ArrayList<>();

    //create field repo in test class if necessary.
    private static ArticleRepository repo;
    private static final ArticleService service = new ArticleService(repo);

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
        service.clear();
    }

    public void addArticles() {
        for (Article article : articles) {
            service.add(article);
        }
    }

    @Test
    public void shouldRetrieveNothingFromEmptyDatabase() throws Exception {
        this.mockMvc.perform(get("/articles"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) //TestUtil.APPLICATION_JSON_UTF8
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldRetrievePostedArticles() throws Exception {
        addArticles();
        this.mockMvc.perform(get("/articles"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(articles.size())));
    }

    @Test
    public void shouldAllowUsToFindArticles() throws Exception {
        addArticles();
        Article article = service.getAll().get(0);
        this.mockMvc.perform(get("/articles/" + article.getId()))
                .andExpect(jsonPath("id", is(article.getId())))
                .andExpect(status().isOk());
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
}