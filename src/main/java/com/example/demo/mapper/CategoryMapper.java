package com.example.demo.mapper;

import com.example.demo.dto.CategoryDto;
import com.example.demo.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);
}
