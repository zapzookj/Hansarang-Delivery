package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.*;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.DeliveryAddressService;
import com.hansarangdelivery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    @PostMapping("/signup")
    public ResultResponseDto<UserResponseDto> SignUp(@Valid @RequestBody SignupRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return new ResultResponseDto<>("회원가입 성공", 200, responseDto);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<UserResponseDto> readProfile(@PathVariable("userId") Long userId) {
        UserResponseDto responseDto = userService.readProfile(userId);
        return new ResultResponseDto<>("조회 성공", 200, responseDto);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<Page<UserResponseDto>> searchProfiles(Pageable pageable) {
        Page<UserResponseDto> responseDtoPage = userService.searchProfiles(pageable);
        return new ResultResponseDto<>("조회 성공",200, responseDtoPage);
    }

    @GetMapping("/my-profile")
    public ResultResponseDto<UserResponseDto> readMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponseDto responseDto = new UserResponseDto(userDetails.getUser());
        return new ResultResponseDto<>("조회 성공", 200, responseDto);
    }

    @PutMapping("/my-profile")
    public ResultResponseDto<UserResponseDto> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @RequestBody UserUpdateDto requestDto) {
        UserResponseDto responseDto = userService.updateProfile(userDetails.getUser().getId(), requestDto);
        return new ResultResponseDto<>("회원 정보 수정 성공", 200, responseDto);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public ResultResponseDto<UserResponseDto> updateRole(@PathVariable("userId")Long userId) {
        UserResponseDto responseDto = userService.updateRole(userId);
        return new ResultResponseDto<>("권한 변경 성공", 200, responseDto);
    }

    @DeleteMapping()
    public ResponseEntity<ResultResponseDto<Void>> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @RequestParam(value = "userId", required = false) Long userId) {
        userService.deleteUser(userDetails.getUser(), userId);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("회원 탈퇴 성공", 200));
    }

    // ====================== DeliveryAddress CRUD API ======================

    @PostMapping("/delivery-addresses") // 배송지 추가 API
    public ResultResponseDto<DeliveryAddressResponseDto> createDeliveryAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody DeliveryAddressRequestDto requestDto) {
        DeliveryAddressResponseDto responseDto = deliveryAddressService.createDeliveryAddress(userDetails.getUser(), requestDto);
        return new ResultResponseDto<>("배송지 추가 완료", 200, responseDto);
    }

    @GetMapping("/delivery-addresses/default") // 로그인한 유저의 기본 배송지 단건 조회
    public ResultResponseDto<DeliveryAddressResponseDto> readMyDeliveryAddress(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        DeliveryAddressResponseDto responseDto = deliveryAddressService.readDeliveryAddress(userDetails.getUser().getId());
        return new ResultResponseDto<>("조회 완료", 200, responseDto);
    }

    @GetMapping("/delivery-addresses") // 로그인한 유저의 배송지 전체 조회
    public ResultResponseDto<List<DeliveryAddressResponseDto>> searchMyDeliveryAddresses(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<DeliveryAddressResponseDto> deliveryAddresses = deliveryAddressService.searchDeliveryAddresses(userDetails.getUser().getId());
        return new ResultResponseDto<>("전체 조회 성공", 200, deliveryAddresses);
    }

    @GetMapping("/{userId}/delivery-addresses/default") // 특정 유저의 기본 배송지 단건 조회 (MANAGER 권한 필요)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<DeliveryAddressResponseDto> readDeliveryAddress(@PathVariable("userId") Long userId) {
        DeliveryAddressResponseDto responseDto = deliveryAddressService.readDeliveryAddress(userId);
        return new ResultResponseDto<>("조회 완료", 200, responseDto);
    }

    @GetMapping("/{userId}/delivery-addresses") // 특정 유저의 배송지 전체 조회 (MANAGER 권한 필요)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<List<DeliveryAddressResponseDto>> searchDeliveryAddresses(@PathVariable("userId") Long userId) {
        List<DeliveryAddressResponseDto> deliveryAddresses = deliveryAddressService.searchDeliveryAddresses(userId);
        return new ResultResponseDto<>("전체 조회 성공", 200, deliveryAddresses);
    }

    @PutMapping("/delivery-addresses/{deliveryAddressId}")
    public ResultResponseDto<DeliveryAddressResponseDto> updateDeliveryAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable("deliveryAddressId") UUID addressId,
                                                         @RequestBody DeliveryAddressRequestDto requestDto) {
        DeliveryAddressResponseDto responseDto =
            deliveryAddressService.updateDeliveryAddress(userDetails.getUser().getId(), addressId, requestDto);
        return new ResultResponseDto<>("배송지 정보 수정 완료", 200, responseDto);
    }

    @DeleteMapping("/delivery-addresses/{deliveryAddressId}")
    public ResultResponseDto<Void> deleteDeliveryAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable("deliveryAddressId") UUID addressId) {
        deliveryAddressService.deleteDeliveryAddress(userDetails.getUser().getId(), addressId);
        return new ResultResponseDto<>("배송지 삭제 완료", 200);
    }
}
