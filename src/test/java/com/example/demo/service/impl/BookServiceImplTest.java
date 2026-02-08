package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
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
import jakarta.persistence.EntityNotFoundException;
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
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Clean Code");
        requestDto.setCategoryIds(List.of(1L));

        Book book = new Book();
        book.setTitle("Clean Code");

        Category category = new Category();
        category.setId(1L);

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Clean Code");

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

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
        BookDto bookDto = new BookDto();
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        Page<BookDto> result = bookService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Find book by valid ID")
    public void findById_ValidId_ReturnsBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        BookDto expected = new BookDto();
        expected.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.findById(bookId);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException when book ID is invalid")
    public void findById_InvalidId_ThrowsException() {
        Long invalidId = 100L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findById(invalidId));
    }

    @Test
    @DisplayName("Update existing book with valid request")
    public void update_ValidIdAndRequest_ReturnsUpdatedBookDto() {
        Long bookId = 1L;
        CreateBookRequestDto updateDto = new CreateBookRequestDto();
        updateDto.setCategoryIds(List.of(1L));

        Book existingBook = new Book();
        existingBook.setId(bookId);
        Category category = new Category();
        category.setId(1L);
        BookDto expectedDto = new BookDto();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.toDto(existingBook)).thenReturn(expectedDto);

        BookDto actualDto = bookService.update(bookId, updateDto);

        assertNotNull(actualDto);
        verify(bookMapper).updateBook(updateDto, existingBook);
        verify(bookRepository).save(existingBook);
    }

    @Test
    @DisplayName("Verify deleteById calls repository")
    public void deleteById_ValidId_CallsRepository() {
        Long bookId = 1L;
        bookService.deleteById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Find all books by category ID")
    public void findAllByCategoryId_ValidId_ReturnsPageOfDtos() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAllByCategoryId(categoryId,
                pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto);

        Page<BookDtoWithoutCategoryIds> result = bookService
                .findAllByCategoryId(categoryId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(bookRepository).findAllByCategoryId(categoryId, pageable);
    }
}
