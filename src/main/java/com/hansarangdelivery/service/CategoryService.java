package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.CategoryRequestDto;
import com.hansarangdelivery.entity.Category;
import com.hansarangdelivery.repository.CategoryRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public void createCategory(@Valid CategoryRequestDto categoryRequestDto) {
        String categoryName = categoryRequestDto.getName();

        // 카테고리 중복 확인
        Optional<Category> existingCategory = categoryRepository.findByName(categoryName);
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        Category newCategory = new Category();
        newCategory.setName(categoryName);

        categoryRepository.save(newCategory);
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }

    public boolean existsById(UUID categoryId){
        return categoryRepository.existsById(categoryId);
    }

    public void saveCategory(Category category) {

        categoryRepository.save(category);
    }


    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 카테고리가 존재하지 않습니다.");
        }
        categoryRepository.deleteById(id);
    }

    public List<String> getAllCategoryNames() {
        List<Category> categories = categoryRepository.findAll(); // 전체 카테고리 조회
        return categories.stream()
            .map(Category::getName) // 카테고리 이름만 추출
            .collect(Collectors.toList()); // 리스트로 변환
    }

    public boolean isCategoryNameExists(String name, UUID excludeId) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        return existingCategory.isPresent() && !existingCategory.get().getId().equals(excludeId);
    }
}
