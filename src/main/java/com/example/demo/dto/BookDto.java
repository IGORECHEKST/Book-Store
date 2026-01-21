package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BookDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "The Great Gatsby")
    private String title;
    @Schema(example = "F. Scott Fitzgerald")
    private String author;
    @Schema(example = "978-3-16-148410-0")
    private String isbn;
    @Schema(example = "19.99")
    private BigDecimal price;
    @Schema(example = "A classic novel set in the 1920s")
    private String description;
    @Schema(example = "http://example.com/cover.jpg")
    private String coverImage;

    private List<Long> categoryIds;
}
