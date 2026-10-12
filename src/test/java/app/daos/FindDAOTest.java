package app.daos;

import app.config.HibernateTestConfig;
import app.entities.Find;
import app.testutils.TestData;
import app.testutils.TestPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) //Klassen oprettes kun én gang per test og bliver ikke oprettet
// for hver deltest/unit test.
class FindDAOTest {

    //Opretter emf med/i test-container.
    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();

    private FindDAO findDAO;
    private TestData testData;

    @BeforeEach
    void beforeEach() {
        findDAO = new FindDAO(emf);
        testData = TestPopulator.populate(emf);
    }

    @AfterAll
    void shutDown() {
        emf.close();
    }

    @Test
    void getAllFindsByUserId() {
        //Returnerer en liste med alle fund, der er gemt under det bruger-id, der søges på.
        Long userId = testData.users.getFirst().getId();
        List<Find> userFinds = findDAO.getAllFindsByUserId(userId);
        assertThat(userFinds, hasSize(4));
    }

    @Test
    void getUserFindsSortedByDate() {
    }

    @Test
    void getUserFindsByCategory() {
        //Returnerer en liste med alle fund, der er gemt under det bruger-id OG kategori-id, der søges på.
        Long userId = testData.users.getFirst().getId();
        Long categoryId = testData.categories.getFirst().getId();
        List<Find> finds = findDAO.getUserFindsByCategory(userId, categoryId);
        assertThat(finds, hasSize(2));
    }

    @Test
    void getUserFindsByFindSpot() {
    }

    @Test
    void getUserFindsByDate() {
        //Returnerer en liste med alle fund, der er gemt under det bruger-id OG den dato, der søges på.
        Long userId = testData.users.get(1).getId();
        LocalDate date = testData.finds.get(4).getDate();
        List<Find> finds = findDAO.getUserFindsByDate(userId, date);
        assertThat(finds, hasSize(1));
    }

    @Test
    void create() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}