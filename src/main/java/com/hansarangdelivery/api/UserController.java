package com.hansarangdelivery.api;

import com.hansarangdelivery.address.dto.DeliveryAddressRequestDto;
import com.hansarangdelivery.address.dto.DeliveryAddressResponseDto;
import com.hansarangdelivery.config.PageableConfig;
import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.global.dto.ResultResponseDto;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.address.service.DeliveryAddressService;
import com.hansarangdelivery.user.service.UserService;
import com.hansarangdelivery.user.dto.SignupRequestDto;
import com.hansarangdelivery.user.dto.UserResponseDto;
import com.hansarangdelivery.user.dto.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final DeliveryAddressService deliveryAddressService;

// ====================== User CRUD API ======================

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @PostMapping("/signup")
    public ResultResponseDto<UserResponseDto> SignUp(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = SignupRequestDto.class))
        )
        @Valid @RequestBody SignupRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return new ResultResponseDto<>("회원가입 성공", 200, responseDto);
    }

    @Operation(summary = "사용자 프로필 조회", description = "특정 사용자의 프로필을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<UserResponseDto> readProfile(
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable("userId") Long userId) {
        UserResponseDto responseDto = userService.readProfile(userId);
        return new ResultResponseDto<>("조회 성공", 200, responseDto);
    }

    @Operation(summary = "사용자 목록 조회", description = "모든 사용자의 프로필을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "size", description = "페이지당 항목 수", example = "10"),
        @Parameter(name = "sort", description = "정렬 기준 (예: createdAt,desc)", example = "createdAt,desc")
    })
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<PageResponseDto<UserResponseDto>> searchProfiles(
        @Parameter(hidden = true) Pageable pageable) {
        PageableConfig.validatePageSize(pageable);
        PageResponseDto<UserResponseDto> responseDtoPage = userService.searchProfiles(pageable);
        return new ResultResponseDto<>("조회 성공",200, responseDtoPage);
    }

    @Operation(summary = "내 프로필 조회", description = "로그인한 사용자의 프로필을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/me")
    public ResultResponseDto<UserResponseDto> readMyProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponseDto responseDto = new UserResponseDto(userDetails.getUser());
        return new ResultResponseDto<>("조회 성공", 200, responseDto);
    }

    @Operation(summary = "프로필 수정", description = "로그인한 사용자의 프로필을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @PutMapping("/me")
    public ResultResponseDto<UserResponseDto> updateProfile(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = UserUpdateDto.class))
        )
        @RequestBody UserUpdateDto requestDto) {
        UserResponseDto responseDto = userService.updateProfile(userDetails.getUser().getId(), requestDto);
        return new ResultResponseDto<>("회원 정보 수정 성공", 200, responseDto);
    }

    @Operation(summary = "사용자 권한 변경", description = "특정 사용자의 권한을 변경합니다.")
    @ApiResponse(responseCode = "200", description = "권한 변경 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public ResultResponseDto<UserResponseDto> updateRole(
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable("userId")Long userId) {
        UserResponseDto responseDto = userService.updateRole(userId);
        return new ResultResponseDto<>("권한 변경 성공", 200, responseDto);
    }

    @Operation(summary = "회원 탈퇴", description = "사용자 계정을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @DeleteMapping()
    public ResponseEntity<ResultResponseDto<Void>> deleteUser(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "삭제할 사용자 ID (관리자용)", example = "1")
        @RequestParam(value = "userId", required = false) Long userId) {
        userService.deleteUser(userDetails.getUser(), userId);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("회원 탈퇴 성공", 200));
    }

// ====================== DeliveryAddress CRUD API ======================

    @Operation(summary = "배송지 추가", description = "새로운 배송지를 추가합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 추가 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @PostMapping("/delivery-addresses")
    public ResultResponseDto<DeliveryAddressResponseDto> createDeliveryAddress(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = DeliveryAddressRequestDto.class))
        )
        @RequestBody DeliveryAddressRequestDto requestDto) {
        DeliveryAddressResponseDto responseDto = deliveryAddressService.createDeliveryAddress(userDetails.getUser(), requestDto);
        return new ResultResponseDto<>("배송지 추가 완료", 200, responseDto);
    }

    @Operation(summary = "기본 배송지 조회", description = "로그인한 사용자의 기본 배송지를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 완료")
    @ApiResponse(responseCode = "404", description = "배송지를 찾을 수 없음", content = @Content)
    @GetMapping("/delivery-addresses/me/default")
    public ResultResponseDto<DeliveryAddressResponseDto> readMyDeliveryAddress(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        DeliveryAddressResponseDto responseDto = deliveryAddressService.readDeliveryAddress(userDetails.getUser().getId());
        return new ResultResponseDto<>("조회 완료", 200, responseDto);
    }

    @Operation(summary = "내 배송지 목록 조회", description = "로그인한 사용자의 모든 배송지를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "전체 조회 성공")
    @GetMapping("/delivery-addresses/me")
    public ResultResponseDto<List<DeliveryAddressResponseDto>> searchMyDeliveryAddresses(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<DeliveryAddressResponseDto> deliveryAddresses = deliveryAddressService.searchDeliveryAddresses(userDetails.getUser().getId());
        return new ResultResponseDto<>("전체 조회 성공", 200, deliveryAddresses);
    }

    @Operation(summary = "특정 사용자의 기본 배송지 조회", description = "특정 사용자의 기본 배송지를 조회합니다. (관리자 권한 필요)")
    @ApiResponse(responseCode = "200", description = "조회 완료")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "배송지를 찾을 수 없음", content = @Content)
    @GetMapping("/{userId}/delivery-addresses/default")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<DeliveryAddressResponseDto> readDeliveryAddress(
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable("userId") Long userId) {
        DeliveryAddressResponseDto responseDto = deliveryAddressService.readDeliveryAddress(userId);
        return new ResultResponseDto<>("조회 완료", 200, responseDto);
    }

    @Operation(summary = "특정 사용자의 배송지 목록 조회", description = "특정 사용자의 모든 배송지를 조회합니다. (관리자 권한 필요)")
    @ApiResponse(responseCode = "200", description = "전체 조회 성공")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @GetMapping("/{userId}/delivery-addresses")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<List<DeliveryAddressResponseDto>> searchDeliveryAddresses(
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable("userId") Long userId) {
        List<DeliveryAddressResponseDto> deliveryAddresses = deliveryAddressService.searchDeliveryAddresses(userId);
        return new ResultResponseDto<>("전체 조회 성공", 200, deliveryAddresses);
    }

    @Operation(summary = "배송지 수정", description = "특정 배송지 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 정보 수정 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "배송지를 찾을 수 없음", content = @Content)
    @PutMapping("/delivery-addresses/{deliveryAddressId}")
    public ResultResponseDto<DeliveryAddressResponseDto> updateDeliveryAddress(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "배송지 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable("deliveryAddressId") UUID addressId,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = DeliveryAddressRequestDto.class))
        )
        @RequestBody DeliveryAddressRequestDto requestDto) {
        DeliveryAddressResponseDto responseDto =
            deliveryAddressService.updateDeliveryAddress(userDetails.getUser().getId(), addressId, requestDto);
        return new ResultResponseDto<>("배송지 정보 수정 완료", 200, responseDto);
    }

    @Operation(summary = "배송지 삭제", description = "특정 배송지를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 삭제 완료")
    @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "배송지를 찾을 수 없음", content = @Content)
    @DeleteMapping("/delivery-addresses/{deliveryAddressId}")
    public ResultResponseDto<Void> deleteDeliveryAddress(
        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Parameter(description = "배송지 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable("deliveryAddressId") UUID addressId) {
        deliveryAddressService.deleteDeliveryAddress(userDetails.getUser().getId(), addressId);
        return new ResultResponseDto<>("배송지 삭제 완료", 200);
    }

}
