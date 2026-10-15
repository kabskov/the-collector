package app.services;

import app.config.HibernateTestConfig;
import app.daos.CategoryDAO;
import app.daos.FindDAO;
import app.daos.UserDAO;
import app.dtos.FindDTO;
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

    private FindService findService;
    private TestData testData;

    @BeforeEach
    void beforeEach() {
        findService = new FindService(emf);
        testData = TestPopulator.populate(emf);
    }

    @AfterAll
    void shutDown() {
        emf.close();
    }

    //Uden søgeord: findId er sat, findSpotName er null, og kategori-id’erne er med i svaret.
    @Test
    void registerFindWithoutFindSpot() throws Exception {
        Long userId = testData.users.getFirst().getId();
        List<Long> categoryIds = List.of(testData.categories.get(0).getId(), testData.categories.get(2).getId());
        FindDTO findDTO = new FindDTO(null, LocalDate.of(2026, 10, 4), "En ny sten", "billedeafsten", userId, categoryIds, null, null);

        FindDTO returnedFindDTO = findService.registerFind(findDTO);

        assertThat(returnedFindDTO.getFindId(), notNullValue());
    }
}