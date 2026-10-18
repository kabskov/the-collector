package app.daos;

import app.entities.Category;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CategoryDAO implements IDAO<Category, Long> {

    private EntityManagerFactory emf;

    public CategoryDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void create(Category type) {

    }

    @Override
    public List<Category> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
         return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        }
    }

    @Override
    public Category getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Category category = em.find(Category.class, id);
            if (category == null) {
                throw new ApiException(404, "Category with id " + id + " not found");
            }
            return category;
        }
    }

    @Override
    public void update(Category type) {

    }

    @Override
    public void delete(Long id) {

    }
}
