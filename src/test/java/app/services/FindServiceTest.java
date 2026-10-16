package app.services;

import app.config.HibernateTestConfig;
import app.daos.CategoryDAO;
import app.daos.FindDAO;
import app.daos.UserDAO;
import app.dtos.FindDTO;
import app.entities.Find;
import app.testutils.TestData;
import app.testutils.TestPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindServiceTest {

    //Opretter emf med/i test-container.
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

    @AfterAll
    void shutDown() {
        emf.close();
    }

    //Tester, at et fund uden fundsted kan registreres/gemmes.
    @Test
    void registerFindWithoutFindSpot() {
        Long userId = testData.users.getFirst().getId();
        List<Long> categoryIds = List.of(testData.categories.get(0).getId(), testData.categories.get(2).getId());
        FindDTO findDTO = new FindDTO(null, LocalDate.of(2026, 10, 4), "En ny sten", "billedeafsten", userId, categoryIds, null, null);

        FindDTO returnedFindDTO = findService.registerFind(findDTO);

        assertThat(returnedFindDTO.getFindId(), notNullValue());
        assertThat(returnedFindDTO.getFindSpotName(), nullValue());
        assertThat(returnedFindDTO.getCategoryIds(), containsInAnyOrder(categoryIds.toArray()));
    }

    //Hent fundet direkte fra databasen via findDAO, og tjek, at det faktisk er gemt.
    @Test
    void registerFindWithoutFindSpotAndCheckDB() {
        String note = "En sten!";
        Long userId = testData.users.get(1).getId();
        List<Long> categoryIds = List.of(testData.categories.get(3).getId());
        FindDTO findDTO = new FindDTO(null, LocalDate.of(2026, 10, 10), note, "billedeafstenen", userId, categoryIds, null, null);

        FindDTO returnedFindDTO = findService.registerFind(findDTO);
        Find find = findDAO.getById(returnedFindDTO.getFindId());

        assertThat(find, notNullValue());
        assertThat(find.getNote(), is(note));
    }

    //Med søgeord og kald til Nominatim (kun i denne ene test på grund af Nominatim-restriktioner).
    @Test
    void registerFindWithFindSpotFromNominatim() {
        Long userId = testData.users.getLast().getId();
        List<Long> categoryIds = List.of(testData.categories.get(2).getId());
        String findSpotKeyword = "Hillerød";
        FindDTO findDTO = new FindDTO(null, LocalDate.of(2025, 8, 17), "En sten fra Hillerød", "billedeURL", userId, categoryIds, findSpotKeyword, null);

        FindDTO returnedFindDTO = findService.registerFind(findDTO);
        Find find = findDAO.getById(returnedFindDTO.getFindId());

        assertThat(find.getFindSpot().getName(), containsStringIgnoringCase(findSpotKeyword));
        assertThat(find.getId(), notNullValue());
    }
}