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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindDAOTest {

    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();

    private FindDAO findDAO;
    private TestData testData;

    @BeforeEach
    void beforeEach() {
        findDAO = new FindDAO(emf);
        testData = TestPopulator.populate(emf);
    }

    //Returns a list with all finds containing the searched user id.
    @Test
    void getAllFindsByUserId() {
        Long userId = testData.users.getFirst().getId();
        List<Find> finds = findDAO.getAllFindsByUserId(userId);

        List<Long> actualFindIds = finds.stream().map(Find::getId).toList();
        List<Long> expectedFindIds = List.of(
                testData.finds.get(0).getId(),
                testData.finds.get(1).getId(),
                testData.finds.get(2).getId(),
                testData.finds.get(3).getId()
        );

        assertThat(finds, hasSize(4));
        assertThat(actualFindIds, containsInAnyOrder(expectedFindIds.toArray()));
    }

    //Returns a list with all finds containing the searched user id AND category id.
    @Test
    void getUserFindsByCategory() {
        Long userId = testData.users.getFirst().getId();
        Long categoryId = testData.categories.getFirst().getId();
        List<Find> finds = findDAO.getUserFindsByCategory(userId, categoryId);

        List<Long> actualFindIds = finds.stream().map(Find::getId).toList();
        List<Long> expectedFindIds = testData.finds.stream()
                .filter(find -> find.getCategories()
                        .stream().anyMatch(category -> category.getId().equals(categoryId))
                        && find.getUser().getId().equals(userId))
                .map(Find::getId).toList();

        assertThat(finds, hasSize(2));
        assertThat(actualFindIds, containsInAnyOrder(expectedFindIds.toArray()));
    }

    //Returns a list with all finds containing the searched user id AND date.
    @Test
    void getUserFindsByDate() {
        Long userId = testData.users.get(1).getId();
        LocalDate date = testData.finds.get(4).getDate();
        List<Find> finds = findDAO.getUserFindsByDate(userId, date);
        assertThat(finds, hasSize(1));
    }
}