package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.CreateCategoryRequestDto;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.BookService;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories - Success")
    void getAll_ReturnsPageOfCategories() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Fiction");

        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(List.of(dto), pageable, 1);

        when(categoryService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name")
                        .value("Fiction"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category - Success")
    void createCategory_ValidRequest_ReturnsCreated() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("History");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("History");

        when(categoryService.save(any())).thenReturn(responseDto);

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("History"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by id - Success")
    void getById_ValidId_ReturnsCategory() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Classic");

        when(categoryService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Classic"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update category - Success")
    void updateCategory_ValidRequest_ReturnsUpdated() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Updated Name");

        CategoryDto responseDto = new CategoryDto();
        responseDto.setId(1L);
        responseDto.setName("Updated Name");

        when(categoryService.update(any(), any())).thenReturn(responseDto);

        mockMvc.perform(put("/categories/1")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete category - Success")
    void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/categories/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
