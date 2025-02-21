package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.CategoryRequestDto;
import com.hansarangdelivery.dto.CategoryResponseDto;
import com.hansarangdelivery.dto.PageResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<CategoryResponseDto> createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto result = categoryService.createCategory(categoryRequestDto);
        return new ResultResponseDto<>("카테고리 생성 성공",200,result);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<CategoryResponseDto> updateCategory(@PathVariable UUID categoryId, @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto result = categoryService.updateCategory(categoryId,categoryRequestDto);
        return new ResultResponseDto<>("카테고리 수정 성공",200, result);
    }


    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<CategoryResponseDto> deleteCategory(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable UUID categoryId) {
        CategoryResponseDto result = categoryService.deleteCategory(userDetails.getUser(),categoryId);
        return new ResultResponseDto<>("카테고리 삭제 성공",200, result);
    }

    @GetMapping("/search")
    public ResultResponseDto<PageResponseDto<CategoryResponseDto>> searchCategories(
        Pageable pageable
        ) {
        PageResponseDto<CategoryResponseDto> result = categoryService.getAllCategory(pageable); // 전체 카테고리 이름 조회
        return new ResultResponseDto<>("카테고리 검색 성공",200,result);
    }


}
