package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.*;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.DeliveryAddressService;
import com.hansarangdelivery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final DeliveryAddressService deliveryAddressService;

    // User CRUD API
    @PostMapping("/signup")
    public ResponseEntity<ResultResponseDto<Void>> SignUp(@Valid @RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("회원가입 성공", 200));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResultResponseDto<UserResponseDto>> getProfile(@PathVariable("userId") Long userId) {
        UserResponseDto responseDto = userService.getProfile(userId);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("조회 성공", 200, responseDto));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ResultResponseDto<Page<UserResponseDto>>> getAllProfile(@RequestParam("page") int page,
                                                                                  @RequestParam("size") int size,
                                                                                  @RequestParam("isAsc") boolean isAsc) {
        Page<UserResponseDto> responseDtoPage = userService.getAllProfile(page-1, size, isAsc);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("조회 성공",200, responseDtoPage));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<ResultResponseDto<UserResponseDto>> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponseDto responseDto = new UserResponseDto(userDetails.getUser());
        return ResponseEntity.status(200).body(new ResultResponseDto<>("조회 성공", 200, responseDto));
    }

    @PutMapping("/my-profile")
    public ResponseEntity<ResultResponseDto<Void>> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @RequestBody UserUpdateDto requestDto) {
        userService.updateProfile(userDetails.getUser().getId(), requestDto);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("회원 정보 수정 성공", 200));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public ResponseEntity<ResultResponseDto<Void>> updateRole(@PathVariable("userId")Long userId) {
        userService.updateRole(userId);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("권한 변경 성공", 200));
    }

    @DeleteMapping()
    public ResponseEntity<ResultResponseDto<Void>> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @RequestParam(value = "userId", required = false) Long userId) {
        userService.deleteUser(userDetails.getUser(), userId);
        return ResponseEntity.status(200).body(new ResultResponseDto<>("회원 탈퇴 성공", 200));
    }

    // DeliveryAddress(배송지) CRUD API

    @PostMapping("/delivery-addresses") // 배송지 추가 API
    public ResultResponseDto<Void> createDeliveryAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody DeliveryAddressRequestDto requestDto) {
        deliveryAddressService.createDeliveryAddress(userDetails.getUser(), requestDto);
        return new ResultResponseDto<>("배송지 추가 완료", 200);
    }

    @GetMapping("/delivery-addresses/default") // 로그인한 유저의 기본 배송지 단건 조회
    public ResultResponseDto<DeliveryAddressResponseDto> getMyDeliveryAddress(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        DeliveryAddressResponseDto responseDto = deliveryAddressService.getDeliveryAddress(userDetails.getUser().getId());
        return new ResultResponseDto<>("조회 완료", 200, responseDto);
    }

    @GetMapping("/delivery-addresses") // 로그인한 유저의 배송지 전체 조회
    public ResultResponseDto<List<DeliveryAddressResponseDto>> getMyAllDeliveryAddresses(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<DeliveryAddressResponseDto> deliveryAddresses = deliveryAddressService.getAllDeliveryAddresses(userDetails.getUser().getId());
        return new ResultResponseDto<>("전체 조회 성공", 200, deliveryAddresses);
    }

    @GetMapping("/{userId}/delivery-addresses/default") // 특정 유저의 기본 배송지 단건 조회 (MANAGER 권한 필요)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<DeliveryAddressResponseDto> getDeliveryAddress(@PathVariable("userId") Long userId) {
        DeliveryAddressResponseDto responseDto = deliveryAddressService.getDeliveryAddress(userId);
        return new ResultResponseDto<>("조회 완료", 200, responseDto);
    }

    @GetMapping("/{userId}/delivery-addresses") // 특정 유저의 배송지 전체 조회 (MANAGER 권한 필요)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResultResponseDto<List<DeliveryAddressResponseDto>> getAllDeliveryAddresses(@PathVariable("userId") Long userId) {
        List<DeliveryAddressResponseDto> deliveryAddresses = deliveryAddressService.getAllDeliveryAddresses(userId);
        return new ResultResponseDto<>("전체 조회 성공", 200, deliveryAddresses);
    }

    @PutMapping("/delivery-addresses/{deliveryAddressId}")
    public ResultResponseDto<Void> updateDeliveryAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable("deliveryAddressId") UUID addressId,
                                                         @RequestBody DeliveryAddressRequestDto requestDto) {
        deliveryAddressService.updateDeliveryAddress(userDetails.getUser().getId(), addressId, requestDto);
        return new ResultResponseDto<>("배송지 정보 수정 완료", 200);
    }

    @DeleteMapping("/delivery-addresses/{deliveryAddressId}")
    public ResultResponseDto<Void> deleteDeliveryAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable("deliveryAddressId") UUID addressId) {
        deliveryAddressService.deleteDeliveryAddress(userDetails.getUser().getId(), addressId);
        return new ResultResponseDto<>("배송지 삭제 완료", 200);
    }
}
