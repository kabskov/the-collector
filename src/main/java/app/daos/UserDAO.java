package app.daos;

import app.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class UserDAO implements IDAO<User, Long> {

    private EntityManagerFactory emf;

    public UserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void create(User type) {

    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getById(Long id) {
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        User user = em.find(User.class, id);
        em.close();

        return user;
    }

    @Override
    public void update(User type) {

    }

    @Override
    public void delete(Long id) {

    }
}
