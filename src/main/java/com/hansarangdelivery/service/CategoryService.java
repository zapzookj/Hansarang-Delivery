package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.CategoryRequestDto;
import com.hansarangdelivery.dto.CategoryResponseDto;
import com.hansarangdelivery.entity.Category;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.exception.DuplicateResourceException;
import com.hansarangdelivery.exception.ResourceNotFoundException;
import com.hansarangdelivery.repository.CategoryRepository;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDto createCategory(@Valid CategoryRequestDto categoryRequestDto) {
        String name = categoryRequestDto.getName();
        boolean exists = categoryRepository.existsByNameIgnoreCase(name);
        if (exists) {
            throw new DuplicateResourceException("카테고리가 이미 존재합니다.");
        }
        Category category = new Category(name);
        categoryRepository.save(category);
        return new CategoryResponseDto(category.getId(),category.getName());
    }

    @Transactional
    public CategoryResponseDto updateCategory(UUID categoryId,
        CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId).orElseThrow(
            () -> new ResourceNotFoundException("수정하려는 카테고리는 존재하지 않습니다."));
        category.update(categoryRequestDto);
        categoryRepository.save(category);
        return new CategoryResponseDto(category.getId(),category.getName());
    }

    public boolean existsById(UUID categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    @Transactional
    public CategoryResponseDto deleteCategory(User user, UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("삭제하려는 카테고리가 존재하지 않습니다."));
        String deletedBy = user.getUsername();// 인증된 사용자의 username을 deletedBy로 사용
        category.delete(LocalDateTime.now(), deletedBy);
        return new CategoryResponseDto(category.getId(),category.getName());
    }

    public Page<CategoryResponseDto> getAllCategory(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(category -> new CategoryResponseDto(category.getId(),
            category.getName()
        ));
    }


    public Category getCategoryById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () -> new ResourceNotFoundException("카테고리가 유효하지 않습니다.")
        );
        return category;
    }
}
