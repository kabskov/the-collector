package app.controllers;

import app.dtos.FindDTO;
import app.services.FindService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

public class FindController {

    private final FindService findService;

    public FindController(EntityManagerFactory emf) {
        this.findService = new FindService(emf);
    }

    public void addRoutes(Javalin app) {
        createFind(app);
        getFindByIdAndUserId(app);
        getAllFindsByUserId(app);
        updateFind(app);
        deleteFind(app);
    }

    private void createFind(Javalin app) {
        app.post("/api/v1/finds", ctx -> {
            FindDTO findDTO = ctx.bodyAsClass(FindDTO.class);
            FindDTO created = findService.registerFind(findDTO);
            ctx.json(created);
            ctx.status(HttpStatus.CREATED);
        });
    }

    private void getFindByIdAndUserId(Javalin app) {
        app.get("/api/v1/users/{userId}/finds/{findId}", ctx -> {
            Long userId = Long.parseLong(ctx.pathParam("userId"));
            Long findId = Long.parseLong(ctx.pathParam("findId"));
            ctx.json(findService.getFindByIdAndUserId(userId, findId));
        });
    }

    private void getAllFindsByUserId(Javalin app) {
        app.get("/api/v1/users/{userId}/finds", ctx -> {
            Long userId = Long.parseLong(ctx.pathParam("userId"));
            ctx.json(findService.getAllFindsByUserId(userId));
        });
    }

    private void updateFind(Javalin app) {
        app.patch("/api/v1/users/{userId}/finds/{findId}", ctx -> {
            Long userId = Long.parseLong(ctx.pathParam("userId"));
            Long findId = Long.parseLong(ctx.pathParam("findId"));

            FindDTO findDTO = ctx.bodyAsClass(FindDTO.class);
            FindDTO updatedFind = findService.updateFind(userId, findId, findDTO);

            ctx.json(updatedFind);
            ctx.status(HttpStatus.OK);
        });
    }

    private void deleteFind(Javalin app) {
        app.delete("/api/v1/users/{userId}/finds/{findId}", ctx -> {
            Long userId = Long.parseLong(ctx.pathParam("userId"));
            Long findId = Long.parseLong(ctx.pathParam("findId"));

            findService.deleteFind(userId, findId);

            ctx.status(HttpStatus.NO_CONTENT);
        });
    }
}
