package com.hansarangdelivery.controller;

import com.hansarangdelivery.dto.*;
import com.hansarangdelivery.security.UserDetailsImpl;
import com.hansarangdelivery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

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



}
