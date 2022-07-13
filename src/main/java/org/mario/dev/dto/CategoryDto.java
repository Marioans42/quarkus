package org.mario.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mario.dev.entity.Category;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Long products;

    public static CategoryDto mapToDto(Category category, Long productsCount) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription(), productsCount);
    }
}
