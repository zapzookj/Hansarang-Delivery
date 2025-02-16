package com.hansarangdelivery.dto;

import com.hansarangdelivery.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "username은 4자 이상 10자 이하의 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String username;

    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?':{}|<>])[A-Za-z\\d!@#$%^&*(),.?':{}|<>]{8,15}$",
        message = "password는 8자 이상 15자 이하의 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @NotBlank
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    private UserRole role;
    private String adminCode = "";

}
