package app.controllers;

import app.dtos.UserDTO;
import app.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

public class UserController {

    private final UserService userService;

    public UserController(EntityManagerFactory emf) {
        this.userService = new UserService(emf);
    }

    public void addRoutes(Javalin app) {
        createUser(app);
        getUserById(app);
    }

    private void createUser(Javalin app) {
        app.post("/api/v1/users", ctx -> {
            UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
            UserDTO created = userService.createUser(userDTO);
            ctx.json(created);
            ctx.status(HttpStatus.CREATED);
        });
    }

    private void getUserById(Javalin app) {
        app.get("/api/v1/users/{id}", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ctx.json(userService.getUserById(id));
        });
    }
}
