package com.hansarangdelivery.api;

import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.restaurant.dto.RestaurantRequestDto;
import com.hansarangdelivery.restaurant.dto.RestaurantResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant", description = "음식점 관리 API")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Operation(summary = "음식점 등록", description = "새로운 음식점을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "가게 등록 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요",content = @Content)
    @ApiResponse(responseCode = "409", description = "중복된 값이 이미 있음",content = @Content)
    @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 넣었을때",content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음",content = @Content)
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<RestaurantResponseDto> createRestaurant(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "기본 음식점",
                        value = """
                        {
                          "name": "맛있는 식당",
                          "location_id": "882e2412-9a3b-40bf-95f5-ba481d5c4fe8",
                          "owner_id": "1",
                          "category_id": "f816c9b7-b6da-4a3f-9c39-bb6b66b4a7b9",
                          "isOpen" : false
                        }
                        """
                    )
                }
            )
        )
        @RequestBody @Valid RestaurantRequestDto requestDto) {
        RestaurantResponseDto response = restaurantService.register(requestDto);
        return new ResultResponseDto<>("가게 등록 성공", 200, response);
    }

    @Operation(summary = "음식점 조회", description = "특정 음식점의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "가게 조회 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요",content = @Content)
    @ApiResponse(responseCode = "409", description = "중복된 값이 이미 있음",content = @Content)
    @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 넣었을때",content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음",content = @Content)
    @GetMapping("/{restaurantId}")
    public ResultResponseDto<RestaurantResponseDto> readRestaurant(
        @Parameter(description = "음식점 ID") @PathVariable UUID restaurantId) {
        RestaurantResponseDto responseDto = restaurantService.getRestaurantInfo(restaurantId);
        return new ResultResponseDto<>("가게 조회 성공", 200, responseDto);
    }

    @Operation(summary = "음식점 수정", description = "음식점 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "가게 수정 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요",content = @Content)
    @ApiResponse(responseCode = "409", description = "중복된 값이 이미 있음",content = @Content)
    @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 넣었을때",content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음",content = @Content)
    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_OWNER')")
    public ResultResponseDto<RestaurantResponseDto> updateRestaurant(
        @Parameter(description = "음식점 ID") @PathVariable UUID restaurantId,
        @RequestBody @Valid RestaurantRequestDto requestDto) {
        RestaurantResponseDto result = restaurantService.updateRestaurant(restaurantId, requestDto);
        return new ResultResponseDto<>("가게 수정 성공", 200, result);
    }

    @Operation(summary = "음식점 삭제", description = "레스토랑을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "가게 삭제 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요",content = @Content)
    @ApiResponse(responseCode = "409", description = "중복된 값이 이미 있음",content = @Content)
    @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 넣었을때",content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음",content = @Content)
    @DeleteMapping("/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<RestaurantResponseDto> deleteRestaurant(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "음식점 ID") @PathVariable UUID restaurantId) {
        RestaurantResponseDto result = restaurantService.deleteRestaurant(userDetails.getUser(), restaurantId);
        return new ResultResponseDto<>("가게 삭제 성공", 200, result);
    }

    @Operation(summary = "음식점 검색", description = "레스토랑을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "가게 검색 성공")
    @ApiResponse(responseCode = "403", description = "권한 필요",content = @Content)
    @ApiResponse(responseCode = "409", description = "중복된 값이 이미 있음",content = @Content)
    @ApiResponse(responseCode = "404", description = "유효하지 않은 값을 넣었을때",content = @Content)
    @ApiResponse(responseCode = "400", description = "값 작성 규칙 지켜지지 않음",content = @Content)
    @Parameters({
        @Parameter(name = "page", description = "현재 페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "size", description = "페이지당 항목 수", example = "10"),
        @Parameter(name = "sort", description = "정렬 기준 (예: name,asc)", example = "name,asc")
    })
    @GetMapping()
    public ResultResponseDto<PageResponseDto<RestaurantResponseDto>> searchRestaurants(
        @Parameter(hidden = true) Pageable pageable,
        @Parameter(description = "가게의 이름에 포함될 단어") @RequestParam(required = false) String search,
        @Parameter(description = "카테고리명") @RequestParam(required = false) String category) {
        PageableConfig.validatePageSize(pageable);
        PageResponseDto<RestaurantResponseDto> restaurants = restaurantService.searchRestaurants(pageable, search, category);
        return new ResultResponseDto<>("가게 검색 성공", 200, restaurants);
    }
}
