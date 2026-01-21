package com.example.demo.mapper;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookDtoWithoutCategoryIds;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.model.Book;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T19:50:14+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        bookDto.setId( book.getId() );
        bookDto.setTitle( book.getTitle() );
        bookDto.setAuthor( book.getAuthor() );
        bookDto.setIsbn( book.getIsbn() );
        bookDto.setPrice( book.getPrice() );
        bookDto.setDescription( book.getDescription() );
        bookDto.setCoverImage( book.getCoverImage() );

        setCategoryIds( bookDto, book );

        return bookDto;
    }

    @Override
    public Book toEntity(CreateBookRequestDto bookDto) {
        if ( bookDto == null ) {
            return null;
        }

        Book book = new Book();

        book.setTitle( bookDto.getTitle() );
        book.setAuthor( bookDto.getAuthor() );
        book.setIsbn( bookDto.getIsbn() );
        book.setPrice( bookDto.getPrice() );
        book.setDescription( bookDto.getDescription() );
        book.setCoverImage( bookDto.getCoverImage() );

        setCategories( book, bookDto );

        return book;
    }

    @Override
    public BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();

        bookDtoWithoutCategoryIds.setId( book.getId() );
        bookDtoWithoutCategoryIds.setTitle( book.getTitle() );
        bookDtoWithoutCategoryIds.setAuthor( book.getAuthor() );
        bookDtoWithoutCategoryIds.setIsbn( book.getIsbn() );
        bookDtoWithoutCategoryIds.setPrice( book.getPrice() );
        bookDtoWithoutCategoryIds.setDescription( book.getDescription() );
        bookDtoWithoutCategoryIds.setCoverImage( book.getCoverImage() );

        return bookDtoWithoutCategoryIds;
    }

    @Override
    public void updateBook(CreateBookRequestDto bookDto, Book book) {
        if ( bookDto == null ) {
            return;
        }

        book.setTitle( bookDto.getTitle() );
        book.setAuthor( bookDto.getAuthor() );
        book.setIsbn( bookDto.getIsbn() );
        book.setPrice( bookDto.getPrice() );
        book.setDescription( bookDto.getDescription() );
        book.setCoverImage( bookDto.getCoverImage() );

        setCategories( book, bookDto );
    }
}
