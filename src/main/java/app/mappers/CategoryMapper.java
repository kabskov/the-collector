package app.mappers;

import app.dtos.CategoryDTO;
import app.entities.Category;

public class CategoryMapper {

    public static CategoryDTO convertToCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
