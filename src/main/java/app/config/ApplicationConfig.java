package app.config;

import app.routes.Routes;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class ApplicationConfig {

    public static Javalin startServer(int port, EntityManagerFactory emf) {
        Javalin app = Javalin.create();
        Routes routes = new Routes(emf);
        routes.getRoutes(app);
        app.start(port);
        return app;
    }
}
