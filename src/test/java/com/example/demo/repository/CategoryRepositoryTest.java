package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isGreaterThan(0L);
        assertThat(savedCategory.getName()).isEqualTo("Fiction");
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
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Non-Fiction");
    }
}
