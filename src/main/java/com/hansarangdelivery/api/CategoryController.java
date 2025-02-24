package com.hansarangdelivery.api;

import com.hansarangdelivery.category.dto.CategoryRequestDto;
import com.hansarangdelivery.category.dto.CategoryResponseDto;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "카테고리 관리 API")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 생성 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "409", description = "중복된 값이 이미 있음", content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음", content = @Content)
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<CategoryResponseDto> createCategory(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = CategoryRequestDto.class),
                examples = {
                    @ExampleObject(
                        name = "카테고리 생성 예시",
                        value = """
                    {
                      "name": "한식"
                    }
                    """
                    )
                }
            )
        )
        @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto result = categoryService.createCategory(categoryRequestDto);
        return new ResultResponseDto<>("카테고리 생성 성공", 200, result);
    }

    @Operation(summary = "카테고리 수정", description = "기존 카테고리 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음", content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음", content = @Content)
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<CategoryResponseDto> updateCategory(
        @Parameter(description = "카테고리 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID categoryId,
        @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto result = categoryService.updateCategory(categoryId, categoryRequestDto);
        return new ResultResponseDto<>("카테고리 수정 성공", 200, result);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요", content = @Content)
    @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음", content = @Content)
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<CategoryResponseDto> deleteCategory(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "카테고리 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable UUID categoryId) {
        CategoryResponseDto result = categoryService.deleteCategory(userDetails.getUser(), categoryId);
        return new ResultResponseDto<>("카테고리 삭제 성공", 200, result);
    }

    @Operation(summary = "카테고리 검색", description = "모든 카테고리를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 검색 성공")
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "size", description = "페이지당 항목 수", example = "10"),
        @Parameter(name = "sort", description = "정렬 기준 (예: name,asc)", example = "name,asc")
    })
    @GetMapping()
    public ResultResponseDto<PageResponseDto<CategoryResponseDto>> searchCategories(
        @Parameter(hidden = true) Pageable pageable) {
        PageResponseDto<CategoryResponseDto> result = categoryService.getAllCategory(pageable);
        return new ResultResponseDto<>("카테고리 검색 성공", 200, result);
    }

}
