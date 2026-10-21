package app.testutils;

import app.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.util.List;

public class TestPopulator {

    public static TestData populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();

            //User:
            User katarina = new User("Katarina", "katarina@mail.dk", "kode1234", Role.USER, LocalDate.of(2026, 6, 1));
            User frederik = new User("Frederik", "frederik@mail.dk", "kode123456", Role.USER, LocalDate.of(2026, 10, 2));

            //Category:
            Category hulsten = new Category("Hulsten");
            Category slåsmutsten = new Category("Slå-smut-sten");
            Category havglas = new Category("Havglas");
            Category pæneSten = new Category("Pæne sten");

            //FindSpot:
            FindSpot helsingør = new FindSpot(56.0360, 12.6136, "Helsingør, Danmark");
            FindSpot karrebæksminde = new FindSpot(55.1864, 11.6554, "Karrebæksminde, Danmark");
            FindSpot birkerød = new FindSpot(55.8372, 12.4280, "Birkerød, Danmark");
            FindSpot roskilde = new FindSpot(55.6415, 12.0803, "Roskilde, Danmark");

            //Find:
            Find find1 = new Find(LocalDate.of(2026, 6, 20), "billede1", "Mit første fund.", katarina);
            find1.addCategory(pæneSten);
            find1.setFindSpot(karrebæksminde);

            Find find2 = new Find(LocalDate.of(2026, 7, 15), "billede2", "En flot hulsten!", katarina);
            find2.addCategory(hulsten);
            find2.addCategory(pæneSten);
            find2.setFindSpot(roskilde);

            Find find3 = new Find(LocalDate.of(2026, 7, 15), "billede3", "Et flot stykke havglas", katarina);
            find3.addCategory(havglas);
            find3.setFindSpot(roskilde);

            Find find4 = new Find(LocalDate.of(2026, 8, 23), "billede4", "Jeg kan ikke huske, hvor jeg fandt denne her hulsten.", katarina);
            find4.addCategory(hulsten);

            Find find5 = new Find(LocalDate.of(2026, 7, 15), "billede5", "...", frederik);
            find5.addCategory(slåsmutsten);
            find5.setFindSpot(roskilde);

            //Lister til TestData:
            List<User> users = List.of(katarina, frederik);
            List<Category> categories = List.of(hulsten, slåsmutsten, havglas, pæneSten);
            List<FindSpot> findSpots = List.of(helsingør, karrebæksminde, birkerød, roskilde);
            List<Find> finds = List.of(find1, find2, find3, find4, find5);

            TestData testData = new TestData();
            testData.users = users;
            testData.categories = categories;
            testData.findSpots = findSpots;
            testData.finds = finds;

            try {
                //TRUNCATE TABLE tømmer tabellerne, der står efter.
                //RESTART IDENTITY nulstiller autogenereret id.
                //CASCADE tømmer alle tabeller, der har en fremmednøgle-reference til tabellerne, der står efter TRUNCATE.
                //Jeg har en join-tabel mellem finds og categories, som ikke er med her. CASCADE løser det, fordi join-tabellen
                //indeholder fremmednøgler til både finds og categories.
                em.createNativeQuery("TRUNCATE TABLE users, categories, find_spots, finds RESTART IDENTITY CASCADE").executeUpdate();

                em.persist(katarina);
                em.persist(frederik);

                em.persist(hulsten);
                em.persist(slåsmutsten);
                em.persist(havglas);
                em.persist(pæneSten);

                em.persist(helsingør);
                em.persist(karrebæksminde);
                em.persist(birkerød);
                em.persist(roskilde);

                em.persist(find1);
                em.persist(find2);
                em.persist(find3);
                em.persist(find4);
                em.persist(find5);

                em.getTransaction().commit();

            } catch (PersistenceException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
            return testData;
        }
    }
}
