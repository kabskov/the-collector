package app.services;

import app.config.HibernateTestConfig;
import app.daos.FindDAO;
import app.dtos.FindDTO;
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
class FindServiceTest {

    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();

    private FindDAO findDAO;
    private FindService findService;
    private TestData testData;

    @BeforeEach
    void beforeEach() {
        findDAO = new FindDAO(emf);
        findService = new FindService(emf);
        testData = TestPopulator.populate(emf);
    }

    //Tests that a find without find spot kan be saved.
    @Test
    void registerFindWithoutFindSpot() {
        Long userId = testData.users.getFirst().getId();
        List<Long> categoryIds = List.of(testData.categories.get(0).getId(), testData.categories.get(2).getId());
        FindDTO findDTO = new FindDTO(null, LocalDate.of(2026, 10, 4), "En ny sten",
                "billedeafsten", userId, categoryIds, null, null);

        FindDTO returnedFindDTO = findService.registerFind(userId, findDTO);

        assertThat(returnedFindDTO.getFindId(), notNullValue());
        assertThat(returnedFindDTO.getFindSpotName(), nullValue());
        assertThat(returnedFindDTO.getCategoryIds(), containsInAnyOrder(categoryIds.toArray()));
    }

    //Fetches the find from the database via FindDAO and checks that the find has been saved.
    @Test
    void registerFindWithoutFindSpotAndCheckDB() {
        String note = "En sten!";
        Long userId = testData.users.get(1).getId();
        List<Long> categoryIds = List.of(testData.categories.get(3).getId());
        FindDTO findDTO = new FindDTO(null, LocalDate.of(2026, 10, 10), note,
                "billedeafstenen", userId, categoryIds, null, null);

        FindDTO returnedFindDTO = findService.registerFind(userId, findDTO);
        Find find = findDAO.getById(returnedFindDTO.getFindId());

        assertThat(find, notNullValue());
        assertThat(find.getNote(), is(note));
    }

    //Tests that a find with find spot can be saved.
    //This test has call to nominatim and is excluded from automated build.
    @Tag("external")
    @Test
    void registerFindWithFindSpotFromNominatim() {
        Long userId = testData.users.getLast().getId();
        List<Long> categoryIds = List.of(testData.categories.get(2).getId());
        String findSpotKeyword = "Hillerød";
        FindDTO findDTO = new FindDTO(null, LocalDate.of(2025, 8, 17),
                "En sten fra Hillerød", "billedeURL", userId, categoryIds, findSpotKeyword, null);

        FindDTO returnedFindDTO = findService.registerFind(userId, findDTO);
        Find find = findDAO.getById(returnedFindDTO.getFindId());

        assertThat(find.getFindSpot().getName(), containsStringIgnoringCase(findSpotKeyword));
        assertThat(find.getId(), notNullValue());
    }
}