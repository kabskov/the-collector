package app;

import app.config.HibernateConfig;
import app.routes.Routes;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    static void main() {
        Javalin app = Javalin.create().start(7070);

        app.get("/", ctx -> ctx.json("Velkommen!"));

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        Routes routes = new Routes(emf);
        routes.getRoutes(app);
    }
}
