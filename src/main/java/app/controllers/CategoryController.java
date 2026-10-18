package app.controllers;

import app.services.CategoryService;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(EntityManagerFactory emf) {
        this.categoryService = new CategoryService(emf);
    }

    public void addRoutes(Javalin app) {
        getAllCategories(app);
    }

    private void getAllCategories(Javalin app) {
        app.get("/api/v1/categories", ctx -> ctx.json(categoryService.getAllCategories()));
    }
}
