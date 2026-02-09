package com.example.demo.util;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.CreateCategoryRequestDto;
import java.math.BigDecimal;
import java.util.List;

public class TestDataHelper {

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("The Hobbit");
        dto.setAuthor("J.R.R. Tolkien");
        dto.setIsbn("9780547928227");
        dto.setPrice(BigDecimal.valueOf(19.99));
        dto.setCategoryIds(List.of(1L));
        return dto;
    }

    public static BookDto createBookDto() {
        BookDto dto = new BookDto();
        dto.setId(1L);
        dto.setTitle("The Hobbit");
        dto.setAuthor("J.R.R. Tolkien");
        dto.setIsbn("9780547928227");
        dto.setPrice(BigDecimal.valueOf(19.99));
        return dto;
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName("Fantasy");
        dto.setDescription("Fantasy books description");
        return dto;
    }

    public static CategoryDto createCategoryDto() {
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Fantasy");
        dto.setDescription("Fantasy books description");
        return dto;
    }
}

