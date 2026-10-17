package app.routes;

import app.controllers.FindController;
import app.exceptions.ApiException;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class Routes {
    private final FindController findController;

    public Routes(EntityManagerFactory emf) {
        this.findController = new FindController(emf);
    }

    public void getRoutes(Javalin app) {
        findController.addRoutes(app);

        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getCode());
            ctx.json(Map.of("status", e.getCode(), "msg", e.getMessage()));
        });
    }
}