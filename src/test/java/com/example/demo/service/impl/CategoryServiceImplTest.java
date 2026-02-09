package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.CreateCategoryRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.util.TestDataHelper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all categories - returns page of categories")
    void findAll_ValidPageable_ReturnsPageOfDtos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Category category = new Category();
        CategoryDto categoryDto = TestDataHelper.createCategoryDto();
        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(eq(pageable))).thenReturn(categoryPage);
        when(categoryMapper.toDto(eq(category))).thenReturn(categoryDto);

        // When
        Page<CategoryDto> result = categoryService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(categoryDto.getName(), result.getContent().get(0).getName());
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Get category by valid ID")
    void getById_ValidId_ReturnsCategoryDto() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        CategoryDto expected = TestDataHelper.createCategoryDto();

        when(categoryRepository.findById(eq(categoryId))).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(eq(category))).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.getById(categoryId);

        // Then
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when category ID is invalid")
    void getById_InvalidId_ThrowsException() {
        // Given
        Long invalidId = 100L;
        when(categoryRepository.findById(eq(invalidId))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(invalidId));
    }

    @Test
    @DisplayName("Save new category")
    void save_ValidDto_ReturnsCategoryDto() {
        // Given
        CreateCategoryRequestDto requestDto = TestDataHelper.createCategoryRequestDto();
        Category category = new Category();
        CategoryDto expected = TestDataHelper.createCategoryDto();

        when(categoryMapper.toEntity(eq(requestDto))).thenReturn(category);
        when(categoryRepository.save(eq(category))).thenReturn(category);
        when(categoryMapper.toDto(eq(category))).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.save(requestDto);

        // Then
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Update existing category")
    void update_ValidId_ReturnsUpdatedDto() {
        // Given
        Long categoryId = 1L;
        CreateCategoryRequestDto requestDto = TestDataHelper.createCategoryRequestDto();
        Category existingCategory = new Category();
        CategoryDto expectedDto = TestDataHelper.createCategoryDto();

        when(categoryRepository.findById(eq(categoryId))).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(eq(existingCategory))).thenReturn(existingCategory);
        when(categoryMapper.toDto(eq(existingCategory))).thenReturn(expectedDto);

        // When
        CategoryDto actualDto = categoryService.update(categoryId, requestDto);

        // Then
        assertNotNull(actualDto);
        assertEquals(expectedDto.getName(), actualDto.getName());
        verify(categoryMapper).updateCategory(eq(requestDto), eq(existingCategory));
    }

    @Test
    @DisplayName("Delete category by ID")
    void deleteById_ValidId_CallsRepository() {
        // Given
        Long categoryId = 1L;

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}

