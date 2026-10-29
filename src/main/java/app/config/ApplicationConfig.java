package app.config;

import app.controllers.SecurityController;
import app.exceptions.ApiException;
import app.routes.Routes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.json.JavalinJackson;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class ApplicationConfig {

    private static Routes routes;

    public static void configuration(JavalinConfig config) {
        config.router.contextPath = "/api/v1"; // Alle routes starter med dette.
        config.http.defaultContentType = "application/json";
        config.router.apiBuilder(routes.getRoutes());
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        config.jsonMapper(new JavalinJackson(mapper, false));
    }

    public static Javalin startServer(int port, EntityManagerFactory emf) {
        routes = new Routes(emf);
        SecurityController securityController = new SecurityController(emf);

        Javalin app = Javalin.create(ApplicationConfig::configuration);
        app.beforeMatched(securityController::authenticate);
        app.beforeMatched(securityController::authorize);
        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getCode());
            ctx.json(Map.of("status", e.getCode(), "msg", e.getMessage()));
        });
        app.exception(NumberFormatException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(Map.of("status", 400, "msg", "Id must be a number"));
        });

        app.start(port);
        return app;
    }
}
