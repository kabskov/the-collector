package app.routes;

import app.controllers.CategoryController;
import app.controllers.FindController;
import app.controllers.SecurityController;
import app.controllers.UserController;
import app.entities.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private final FindController findController;
    private final CategoryController categoryController;
    private final UserController userController;
    private final SecurityController securityController;

    public Routes(EntityManagerFactory emf) {
        this.findController = new FindController(emf);
        this.categoryController = new CategoryController(emf);
        this.userController = new UserController(emf);
        this.securityController = new SecurityController(emf);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/auth", () -> {
                post("/login", securityController::login, Role.ANYONE);
            });
            path("/categories", () -> {
                get(categoryController::getAllCategories, Role.ANYONE);
            });
            path("/finds", () -> {
                post(findController::registerFind, Role.USER);
            });
            path("/users", () -> {
                post(userController::createUser, Role.ANYONE);
                path("/{userId}", () -> {
                    get(userController::getUserById, Role.USER);
                    path("/finds", () -> {
                        get(findController::getAllFindsByUserId, Role.USER);
                        path("/{findId}", () -> {
                            get(findController::getFindByIdAndUserId, Role.USER);
                            patch(findController::updateFind, Role.USER);
                            delete(findController::deleteFind, Role.USER);
                        });
                    });
                });
            });
        };
    }
}