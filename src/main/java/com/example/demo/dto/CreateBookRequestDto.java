package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    private static final int MIN_PRICE = 0;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    @NotNull(message = "Price cannot be null")
    @Min(MIN_PRICE)
    private BigDecimal price;

    private String description;

    private String coverImage;

    @NotEmpty(message = "At least one category id must be provided")
    private List<Long> categoryIds;

}
