package app.config;

import app.entities.Category;
import app.entities.Find;
import app.entities.FindSpot;
import app.entities.User;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(FindSpot.class);
        configuration.addAnnotatedClass(Find.class);
    }
}
