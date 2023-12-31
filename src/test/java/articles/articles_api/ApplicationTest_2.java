package articles.articles_api;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest_2 {
    @Autowired
    private ArticleService service;
    private static Connection connection;
    private static Statement statement;
    private static final List<Article> articles = new ArrayList<Article>();

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
            this.service.add(article);
        }
    }

    @Test
    public void shouldRetrieveNothingFromEmptyDatabase() throws Exception {
        this.mockMvc.perform(get("/articles"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))  //TestUtil.APPLICATION_JSON_UTF8
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
        Article article = this.service.getAll().get(0);
        this.mockMvc.perform(get("/articles/" + article.getId()))
                .andExpect(jsonPath("id", is(article.getId())))
                .andExpect(status().isOk());
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