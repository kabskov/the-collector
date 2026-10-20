package app.controllers;

import app.dtos.UserDTO;
import app.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

public class UserController {

    private final UserService userService;

    public UserController(EntityManagerFactory emf) {
        this.userService = new UserService(emf);
    }

    public void createUser(Context ctx) {
            UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
            UserDTO created = userService.createUser(userDTO);
            ctx.json(created);
            ctx.status(HttpStatus.CREATED);
    }

    public void getUserById(Context ctx) {
            Long userId = Long.parseLong(ctx.pathParam("userId"));
            SecurityController.requireOwner(ctx, userId);
            ctx.json(userService.getUserById(userId));
    }
}
