package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Знайти всі книги за ID категорії (валідна категорія)")
    @Sql(scripts = {
            "classpath:database/categories/add-fantasy-category.sql",
            "classpath:database/books/add-books-to-fantasy.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_ValidId_ReturnsPageOfBooks() {
        // Given
        Long categoryId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findAllByCategoryId(categoryId, pageable);

        // Then
        assertTrue(result.getContent().size() > 0, "Список не має бути порожнім");
        assertEquals(1, result.getTotalElements(), "Кількість елементів має бути 1");
        assertEquals("The Hobbit", result.getContent().get(0).getTitle(),
                "Назва книги неспівпадає");
    }

    @Test
    @DisplayName("Порожній результат для неіснуючої категорії")
    void findAllByCategoryId_NonExistingId_ReturnsEmptyPage() {
        // Given
        Long nonExistingId = 999L;
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findAllByCategoryId(nonExistingId, pageable);

        // Then
        assertEquals(0, result.getContent().size(),
                "Результат повинен бути порожнім для невірного ID");
    }
}
