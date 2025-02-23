package com.hansarangdelivery.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponseDto {
    private UUID id;
    private String name;
}
