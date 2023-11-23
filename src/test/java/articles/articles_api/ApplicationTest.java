package articles.articles_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    @Autowired
    private ArticleService service;
    @Value("${spring.datasource.url}")
    private static String databaseUrl;

    // Connection and statement for database setup/teardown
    private static Connection connection;
    private static Statement statement;
    private static final List<Article> articles = new ArrayList<Article>();

    /**
     * Injects the mock Library.
     */
    @Autowired
    private MockMvc mockMvc;

    @BeforeClass
    public static void setUp() throws SQLException {
        // String In_memory_Connection = "jdbc:sqlite::memory:"; <- sqlite
        String inMemoryConnection = "jdbc:h2:mem:testdb;USER=sa;PASSWORD=";
        connection = DriverManager.getConnection(inMemoryConnection);
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS articles " +
                "(id INT PRIMARY KEY AUTO_INCREMENT, body TEXT, title TEXT)");
    }

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

    @AfterClass
    public static void tearDown() throws SQLException {
        // Drop the 'articles' table
        statement.execute("DROP TABLE articles");
        statement.close();
        connection.close();
    }

    public void addArticles() {
        for (Article article : articles) {
            service.add(article);
        }
    }

    @Test
    public void shouldLetUsPostArticles_Modified_UTF_8() throws Exception {
        var mocker = this.mockMvc;
        for (Article article : articles) {
            this.mockMvc.perform(post("/articles")
                            .content(asJsonString(article))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("title", is(article.getTitle())));
        }
    }

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
        List<Article> all = this.service.getAll();
        Article article = this.service.getAll().get(0);
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
            this.mockMvc.perform(delete("/articles/" + article.getId()))
                    .andExpect(status().isNoContent());
        }
        assertEquals("Should remove all articles", 0, this.service.getAll().size());
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




