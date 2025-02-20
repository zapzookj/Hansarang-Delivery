package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.CategoryRequestDto;
import com.hansarangdelivery.dto.CategoryResponseDto;
import com.hansarangdelivery.dto.ResultResponseDto;
import com.hansarangdelivery.entity.Category;
import com.hansarangdelivery.repository.CategoryRepository;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public ResultResponseDto createCategory(@Valid CategoryRequestDto categoryRequestDto) {
        String name = categoryRequestDto.getName();
        boolean exists = categoryRepository.existsByNameAndDeletedAtIsNull(name);
        if(exists){
            throw new IllegalArgumentException("해당 카테고리가 이미 존재합니다.");
        }
        Category category = new Category(name);
        categoryRepository.save(category);
        return new ResultResponseDto("카테고리를 생성했습니다.",200);
    }

    @Transactional
    public ResultResponseDto updateCategory(UUID categoryId,CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId).orElseThrow(
            ()-> new IllegalArgumentException("수정하려는 카테고리는 존재하지 않습니다.")
        );
        category.update(categoryRequestDto);
        categoryRepository.save(category);
        return new ResultResponseDto("카테고리를 수정했습니다.",200);
    }

    public boolean existsById(UUID categoryId){
        return categoryRepository.existsById(categoryId);
    }

    @Transactional
    public ResultResponseDto deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 카테고리가 존재하지 않습니다.");
        }
        Category category = categoryRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("삭제하려는 카테고리가 존재하지 않습니다.")
        );
        // 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String deletedBy = authentication.getName(); // 인증된 사용자의 username을 deletedBy로 사용
        // 삭제 처리(삭제자와 삭제날짜)
        category.delete(LocalDateTime.now(),deletedBy);

        return new ResultResponseDto("카테고리를 삭제했습니다.",200);
    }

    public ResultResponseDto<Page<CategoryResponseDto>> getAllCategory(int page, int size, String sort, String direction) {
        // 카테고리 전체 반환하는 메서드
        page = page-1;

        // 파라미터 유효성 검사(size, sort, direction)
        if (size != 10 && size != 30 && size != 50) {
            size = 10; // 허용되지 않은 size는 기본값 10으로 설정
        }

        // sort 값이 여러 개 들어올 수 있도록 처리
        List<String> validSortFields = List.of("createdAt", "updatedAt");
        List<Order> orders = new ArrayList<>();

        for (String sortField : sort.split(",")) { // "createdAt", "updatedAt" 정렬 기준을 지원
            if (validSortFields.contains(sortField)) {
                orders.add(new Order(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
            }
        }

        if (orders.isEmpty()) {
            orders.add(new Order(Sort.Direction.DESC, "createdAt"));  // 기본: 생성일 최신순
            orders.add(new Order(Sort.Direction.DESC, "updatedAt"));  // 생성일 같을때 : 수정일 최신순
        }

        // Pageable 생성
        Sort sortBy = Sort.by(orders);
        Pageable pageable = PageRequest.of(page, size, sortBy);

        // 카테고리 조회 (deletedAt이 null인 카테고리만)
        Page<Category> categoryPage = categoryRepository.findByDeletedAtIsNull(pageable);
        Page<CategoryResponseDto> categoryResponseDtos = categoryPage.map(category -> new CategoryResponseDto(
            category.getName()
        ));

        return new ResultResponseDto("전체 카테고리 이름 조회 성공", 200,categoryResponseDtos); // 결과 반환
    }


    public Category getCategoryById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
            () ->  new IllegalArgumentException("카테고리 없음")
        );
        return category;
    }
}
