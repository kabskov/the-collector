package app.controllers;

import app.services.CategoryService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(EntityManagerFactory emf) {
        this.categoryService = new CategoryService(emf);
    }

    public void getAllCategories(Context ctx) {
        ctx.json(categoryService.getAllCategories());
    }
}
