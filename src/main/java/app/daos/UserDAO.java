package app.daos;

import app.entities.User;
import app.exceptions.ApiException;
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
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, id);
            if (user == null) {
                throw new ApiException(404, "User with id " + id + " not found");
            }
            return user;
        }
    }

    @Override
    public void update(User type) {

    }

    @Override
    public void delete(Long id) {

    }
}
