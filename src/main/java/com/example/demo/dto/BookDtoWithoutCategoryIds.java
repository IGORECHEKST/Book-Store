package com.example.demo.dto;

import lombok.Data;

@Data
public class BookDtoWithoutCategoryIds {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private java.math.BigDecimal price;
    private String description;
    private String coverImage;
}
