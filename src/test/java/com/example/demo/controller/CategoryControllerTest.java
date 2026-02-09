package com.example.demo.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.dto.CreateCategoryRequestDto;
import com.example.demo.util.TestDataHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories - Success")
    @Sql(scripts = "classpath:database/categories/add-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ReturnsPageOfCategories() throws Exception {
        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category - Success")
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequest_ReturnsCreated() throws Exception {
        CreateCategoryRequestDto requestDto = TestDataHelper.createCategoryRequestDto();

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(requestDto.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category - Failure (Negative case)")
    void createCategory_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateCategoryRequestDto invalidDto = new CreateCategoryRequestDto();
        invalidDto.setName("");

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by id - Success")
    @Sql(scripts = "classpath:database/categories/add-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getById_ValidId_ReturnsCategory() throws Exception {
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update category - Success")
    @Sql(scripts = "classpath:database/categories/add-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidRequest_ReturnsUpdated() throws Exception {
        CreateCategoryRequestDto updateDto = new CreateCategoryRequestDto();
        updateDto.setName("Updated Fantasy");

        mockMvc.perform(put("/categories/1")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Fantasy"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete category - Success")
    @Sql(scripts = "classpath:database/categories/add-fantasy-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/categories/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
