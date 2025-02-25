package com.hansarangdelivery.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "사용자 정보 수정 요청 DTO")
public class UserUpdateDto {
    @Schema(description = "새로운 사용자 이름", example = "user123", required = true, minLength = 4, maxLength = 10)
    @NotBlank
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "username은 4자 이상 10자 이하의 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String username;

    @Schema(description = "새로운 이메일 주소", example = "user@example.com", required = true)
    @NotBlank
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;
}
