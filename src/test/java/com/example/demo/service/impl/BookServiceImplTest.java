package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookDtoWithoutCategoryIds;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.BookRepository;
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
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save book with valid request and categories")
    public void save_ValidRequest_ReturnsBookDto() {
        // Given
        CreateBookRequestDto requestDto = TestDataHelper.createBookRequestDto();
        BookDto expected = TestDataHelper.createBookDto();

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        Category category = new Category();
        category.setId(1L);

        when(bookMapper.toEntity(eq(requestDto))).thenReturn(book);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookRepository.save(eq(book))).thenReturn(book);
        when(bookMapper.toDto(eq(book))).thenReturn(expected);

        // When
        BookDto actual = bookService.save(requestDto);

        // Then
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Find all books - returns page of books")
    public void findAll_ValidPageable_ReturnsPageOfDtos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        BookDto bookDto = TestDataHelper.createBookDto();
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(eq(pageable))).thenReturn(bookPage);
        when(bookMapper.toDto(eq(book))).thenReturn(bookDto);

        // When
        Page<BookDto> result = bookService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(bookDto, result.getContent().get(0));
    }

    @Test
    @DisplayName("Find book by valid ID")
    public void findById_ValidId_ReturnsBookDto() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        BookDto expected = TestDataHelper.createBookDto();

        when(bookRepository.findById(eq(bookId))).thenReturn(Optional.of(book));
        when(bookMapper.toDto(eq(book))).thenReturn(expected);

        // When
        BookDto actual = bookService.findById(bookId);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update existing book with valid request")
    public void update_ValidIdAndRequest_ReturnsUpdatedBookDto() {
        // Given
        Long bookId = 1L;
        CreateBookRequestDto updateDto = TestDataHelper.createBookRequestDto();
        Book existingBook = new Book();
        existingBook.setId(bookId);

        Category category = new Category();
        category.setId(1L);
        BookDto expectedDto = TestDataHelper.createBookDto();

        when(bookRepository.findById(eq(bookId))).thenReturn(Optional.of(existingBook));
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookRepository.save(eq(existingBook))).thenReturn(existingBook);
        when(bookMapper.toDto(eq(existingBook))).thenReturn(expectedDto);

        // When
        BookDto actualDto = bookService.update(bookId, updateDto);

        // Then
        assertNotNull(actualDto);
        verify(bookMapper).updateBook(eq(updateDto), eq(existingBook));
        verify(bookRepository).save(existingBook);
    }

    @Test
    @DisplayName("Find all books by category ID")
    public void findAllByCategoryId_ValidId_ReturnsPageOfDtos() {
        // Given
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAllByCategoryId(eq(categoryId), eq(pageable))).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategories(eq(book))).thenReturn(dto);

        // When
        Page<BookDtoWithoutCategoryIds> result = bookService.findAllByCategoryId(categoryId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }
}
