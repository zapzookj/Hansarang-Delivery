package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.CategoryRequestDto;
import com.hansarangdelivery.dto.CategoryResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.entity.Category;
import com.hansarangdelivery.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto create(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        ResultResponseDto result = categoryService.createCategory(categoryRequestDto);
        return result;
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto updateCategory(@PathVariable UUID categoryId, @RequestBody CategoryRequestDto categoryRequestDto) {
        ResultResponseDto result = categoryService.updateCategory(categoryId,categoryRequestDto);
        return result;
    }


    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto deleteCategory(@PathVariable UUID categoryId) {
        ResultResponseDto result = categoryService.deleteCategory(categoryId);
        return result;
    }

    @GetMapping("/search")
    public ResultResponseDto<Page<CategoryResponseDto>> searchAllCategories(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sort,
        @RequestParam(defaultValue = "desc") String direction
        ) {
        ResultResponseDto result = categoryService.getAllCategory(page,size,sort,direction); // 전체 카테고리 이름 조회
        return result;
    }


}
