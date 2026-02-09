package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.Category;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Збереження категорії в БД")
    void saveCategory_ShouldReturnSavedCategory() {
        // Given
        Category category = new Category();
        category.setName("Fiction");
        category.setDescription("Fiction books");

        // When
        Category savedCategory = categoryRepository.save(category);

        // Then
        assertNotNull(savedCategory, "Збережена категорія не має бути null");
        assertNotNull(savedCategory.getId(), "ID має бути згенерований");
        assertTrue(savedCategory.getId() > 0, "ID має бути більше 0");
        assertEquals("Fiction", savedCategory.getName(),
                "Назва категорії не співпадає");
    }

    @Test
    @DisplayName("Пошук по ID")
    void findById_ShouldReturnCategory_WhenIdExists() {
        // Given
        Category category = new Category();
        category.setName("Non-Fiction");
        Category saved = categoryRepository.save(category);

        // When
        Optional<Category> found = categoryRepository.findById(saved.getId());

        // Then
        assertTrue(found.isPresent(), "Категорія має бути знайдена");
        assertEquals("Non-Fiction", found.get().getName(),
                "Назва знайденої категорії не співпадає");
    }
}
