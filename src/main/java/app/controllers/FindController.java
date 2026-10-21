package app.controllers;

import app.dtos.FindDTO;
import app.services.FindService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

public class FindController {

    private final FindService findService;

    public FindController(EntityManagerFactory emf) {
        this.findService = new FindService(emf);
    }

    public void registerFind(Context ctx) {
        Long userId = SecurityController.getUserId(ctx);
        FindDTO findDTO = ctx.bodyAsClass(FindDTO.class);
        FindDTO created = findService.registerFind(userId, findDTO);
        ctx.json(created);
        ctx.status(HttpStatus.CREATED);
    }

    public void getFindByIdAndUserId(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("userId"));
        Long findId = Long.parseLong(ctx.pathParam("findId"));
        SecurityController.requireOwner(ctx, userId);
        ctx.json(findService.getFindByIdAndUserId(userId, findId));
    }

    public void getAllFindsByUserId(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("userId"));
        SecurityController.requireOwner(ctx, userId);
        ctx.json(findService.getAllFindsByUserId(userId));
    }

    public void updateFind(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("userId"));
        Long findId = Long.parseLong(ctx.pathParam("findId"));
        SecurityController.requireOwner(ctx, userId);

        FindDTO findDTO = ctx.bodyAsClass(FindDTO.class);
        FindDTO updatedFind = findService.updateFind(userId, findId, findDTO);

        ctx.json(updatedFind);
        ctx.status(HttpStatus.OK);
    }

    public void deleteFind(Context ctx) {
        Long userId = Long.parseLong(ctx.pathParam("userId"));
        Long findId = Long.parseLong(ctx.pathParam("findId"));
        SecurityController.requireOwner(ctx, userId);

        findService.deleteFind(userId, findId);

        ctx.status(HttpStatus.NO_CONTENT);
    }
}