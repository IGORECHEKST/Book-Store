package com.example.demo.mapper;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.model.Book;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto bookDto);

    void updateBook(CreateBookRequestDto bookDto, @MappingTarget Book book);
}
