package app.services;

import app.daos.CategoryDAO;
import app.dtos.CategoryDTO;
import app.entities.Category;
import app.mappers.CategoryMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(EntityManagerFactory emf) {
        this.categoryDAO = new CategoryDAO(emf);
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryDAO.getAll();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for (Category category : categories) {
            categoryDTOS.add(CategoryMapper.convertToCategoryDTO(category));
        }

        return categoryDTOS;
    }
}
