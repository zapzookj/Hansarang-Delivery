package com.hansarangdelivery.user.dto;

import com.hansarangdelivery.user.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @Schema(description = "사용자 이름", example = "user123", required = true, minLength = 4, maxLength = 10)
    @NotBlank
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "username은 4자 이상 10자 이하의 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String username;

    @Schema(description = "비밀번호", example = "Pass123!", required = true, minLength = 8, maxLength = 15, format = "password")
    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?':{}|<>])[A-Za-z\\d!@#$%^&*(),.?':{}|<>]{8,15}$",
        message = "password는 8자 이상 15자 이하의 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @Schema(description = "이메일 주소", example = "user@example.com", required = true)
    @NotBlank
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @Schema(description = "사용자 역할", example = "CUSTOMER", allowableValues = {"CUSTOMER", "OWNER", "MANAGER"})
    private UserRole role;

    @Schema(description = "관리자 코드 (관리자 계정 생성 시 필요)", example = "ADMIN123", required = false)
    private String adminCode = "";

}
