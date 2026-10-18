package app.routes;

import app.controllers.CategoryController;
import app.controllers.FindController;
import app.exceptions.ApiException;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class Routes {
    private final FindController findController;
    private final CategoryController categoryController;

    public Routes(EntityManagerFactory emf) {
        this.findController = new FindController(emf);
        this.categoryController = new CategoryController(emf);
    }

    public void getRoutes(Javalin app) {
        findController.addRoutes(app);
        categoryController.addRoutes(app);

        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getCode());
            ctx.json(Map.of("status", e.getCode(), "msg", e.getMessage()));
        });
    }
}