package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemUpdateDto {
    @Min(1)
    private int quantity;
}
