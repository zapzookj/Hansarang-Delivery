package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.CategoryRequestDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.entity.Category;
import com.hansarangdelivery.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
//    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ResultResponseDto> create(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResultResponseDto("카테고리 생성 성공", 200));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ResultResponseDto> updateCategory(@PathVariable UUID categoryId, @RequestBody Category updatedCategory) {
        Optional<Category> existingCategory = categoryService.getCategoryById(categoryId);

        if (existingCategory.isPresent()) {
            // 이름 중복 확인
            if (categoryService.isCategoryNameExists(updatedCategory.getName(), categoryId)) {
                return ResponseEntity.ok(new ResultResponseDto("이미 존재하는 카테고리 이름입니다.", 204));
            }

            Category categoryToUpdate = existingCategory.get();
            categoryToUpdate.setName(updatedCategory.getName());
            categoryService.saveCategory(categoryToUpdate);

            return ResponseEntity.ok(new ResultResponseDto("카테고리 변경 성공", 200));
        } else {
            return ResponseEntity.ok(new ResultResponseDto("카테고리를 찾을 수 없습니다.", 204));
        }
    }


    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResultResponseDto> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new ResultResponseDto("카테고리 삭제 성공", 204)); // 삭제 성공 응답
    }

    @GetMapping("/search")
    public ResponseEntity<ResultResponseDto> searchAllCategoryNames() {
        List<String> categoryNames = categoryService.getAllCategoryNames(); // 전체 카테고리 이름 조회
        return ResponseEntity.ok(new ResultResponseDto("전체 카테고리 이름 조회 성공", 200,categoryNames)); // 결과 반환
    }


}
