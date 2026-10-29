package app.daos;

import app.entities.Category;
import app.entities.Find;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class FindDAO implements IDAO<Find, Long> {

    private final EntityManagerFactory emf;

    public FindDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //Returns a list of finds belonging to a specific user.
    public List<Find> getAllFindsByUserId(Long userId) {
        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Find> query = em.createQuery("SELECT f FROM Find f LEFT JOIN FETCH f.categories WHERE f.user.id = :userId", Find.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        }
    }

    //Returns a list of finds belonging to a specific user and sorted by date.
    public List<Find> getUserFindsSortedByDate(Long userId, boolean ascending) {
        try (EntityManager em = emf.createEntityManager()) {

            //Hvis metoden kaldes med "true", sættes variablen til ASC, hvis den kaldes med "false", sættes den til DESC.
            String direction = ascending ? "ASC" : "DESC";
            TypedQuery<Find> query = em.createQuery("SELECT f FROM Find f WHERE f.user.id = :userId ORDER BY f.date " + direction, Find.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        }
    }

    //Returns a list of finds from a specific category and belonging to a specific user.
    public List<Find> getUserFindsByCategory(Long userId, Long categoryId) {
        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Find> query = em.createQuery("SELECT f FROM Find f JOIN f.categories c WHERE f.user.id = :userId AND c.id = :categoryId", Find.class);
            query.setParameter("userId", userId);
            query.setParameter("categoryId", categoryId);
            return query.getResultList();
        }
    }

    //Returns a list of finds from a specific find spot and belonging to a specific user.
    public List<Find> getUserFindsByFindSpot(Long userId, Long findSpotId) {
        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Find> query = em.createQuery("SELECT f FROM Find f WHERE f.user.id = :userId AND f.findSpot.id = :findSpotId", Find.class);
            query.setParameter("userId", userId);
            query.setParameter("findSpotId", findSpotId);
            return query.getResultList();
        }
    }

    //Returns a list of finds from a specific date and belonging to a specific user.
    public List<Find> getUserFindsByDate(Long userId, LocalDate date) {
        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Find> query = em.createQuery("SELECT f FROM Find f WHERE f.user.id = :userId AND f.date = :date", Find.class);
            query.setParameter("userId", userId);
            query.setParameter("date", date);
            return query.getResultList();
        }
    }

    @Override
    public void create(Find find) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                em.persist(find);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new ApiException(500, "Could not create find", e);
            }
        }
    }

    @Override
    public List<Find> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT f FROM Find f", Find.class).getResultList();
        }
    }

    @Override
    public Find getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Find> query = em.createQuery("SELECT f FROM Find f LEFT JOIN FETCH f.categories WHERE f.id = :id", Find.class);
            query.setParameter("id", id);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                throw new ApiException(404, "Find with id " + id + " not found");
            }
        }
    }

    @Override
    public void update(Find find) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Find existingFind = em.find(Find.class, find.getId());
                if (existingFind == null) {
                    throw new ApiException(404, "Find with id " + find.getId() + " not found");
                }
                existingFind.setDate(find.getDate());
                existingFind.setPhotoURL(find.getPhotoURL());
                existingFind.setNote(find.getNote());
                existingFind.getCategories().clear();
                for (Category category : find.getCategories()) {
                    existingFind.addCategory(category);
                }
                em.getTransaction().commit();
            } catch (ApiException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new ApiException(500, "Could not update find", e);
            }
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                Find existingFind = em.find(Find.class, id);
                if (existingFind == null) {
                    throw new ApiException(404, "Find with id " + id + " not found");
                }
                em.remove(existingFind);
                em.getTransaction().commit();
            } catch (ApiException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new ApiException(500, "Could not delete find", e);
            }
        }
    }
}
