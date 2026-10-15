package app.daos;

import app.entities.Category;
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
        return List.of();
    }

    @Override
    public Category getById(Long id) {
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Category category = em.find(Category.class, id);
        em.close();

        return category;
    }

    @Override
    public void update(Category type) {

    }

    @Override
    public void delete(Long id) {

    }
}
